(ns me.moocar.java.io
  (:require [clojure.java.io :as jio])
  (:import (java.io File)
           (java.nio.charset Charset)
           (java.nio.file Files Path StandardOpenOption LinkOption Paths)))

(defn- ^String encoding [opts]
  (or (:encoding opts) "UTF-8"))

(defn- ^Boolean append? [opts]
  (boolean (:append opts)))

(defn- ^Charset charset [opts]
  (Charset/forName (encoding opts)))

(defn- opts-to-vec
  [opts opts-mapping]
  (reduce
   (fn [result [k v]]
     (if (and (true? v) (contains? opts-mapping k))
       (conj result (get opts-mapping k))
       result))
   []
   opts))

(def ^:private open-options
  {:append StandardOpenOption/APPEND})

(defn- open-option-array
  ([] (open-option-array nil))
  ([opts]
   (into-array StandardOpenOption (opts-to-vec opts open-options))))

(extend-type Path

  jio/IOFactory
  (make-reader [path opts]
    (Files/newBufferedReader path (charset opts)))
  (make-writer [path opts]
    (Files/newBufferedWriter path (charset opts) (open-option-array opts)))
  (make-input-stream [path opts]
    (jio/make-input-stream
     (Files/newInputStream path (open-option-array)) opts))
  (make-output-stream [path opts]
    (jio/make-output-stream
     (Files/newOutputStream path (open-option-array opts)) opts))

  jio/Coercions
  (as-file [path]
    (.toFile path))
  (as-url [path]
    (.toURL (.toURI path))))

(defprotocol AsPathCoercion
  (as-path [x]
    "Coerces x to a path"))

(extend-protocol AsPathCoercion
  String
  (as-path [string]
    (Paths/get string (into-array String [])))
  File
  (as-path [file]
    (.toPath file)))

(defn ^Path path
  ([arg]
   (as-path arg))
  ([parent & child]
   (Paths/get parent (into-array String child))))
