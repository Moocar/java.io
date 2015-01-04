# java.io

JK7 java.nio.file.Path compatibility for clojure.java.io

## Dependency information

This library is hosted on [Clojars](https://clojars.org/) Releases. [Leiningen](https://github.com/technomancy/leiningen/) dependency:

```clojure
[me.moocar/java.io "0.1.0"]
```

## Usage

[java.nio.file.Path](http://docs.oracle.com/javase/7/docs/api/java/nio/file/Path.html)
is an abstraction for File paths that was introduced in JDK7. This
library provides compatibility between
[clojure.java.io](https://clojure.github.io/clojure/clojure.java.io-api.html)
and Paths.

Example usage below

```clojure
(require 'me.moocar.java.io)
(require '[clojure.java.io :as jio])
(import '(java.nio.file Paths))

(def p (Paths/get "/tmp" (into-array ["foo"])))

(with-open [writer (jio/writer p)]
  (.write writer "Hello!\n"))

(with-open [writer (jio/writer p :append true)]
  (.write writer "How's it goin?\n"))

(with-open [reader (jio/reader p)]
  (println (slurp reader)))
Hello!
How's it goin?

(println (type (jio/as-file p)))
java.io.File
```

A convenience function is also provided to create paths

```clojure
(me.moocar.java.io/path "/tmp/foo")
#<UnixPath /tmp/foo>
(me.moocar.java.io/path "/tmp" "foo" "bar")
#<UnixPath /tmp/foo/bar>
```

## License

Copyright © 2015 Anthony Marcar

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
