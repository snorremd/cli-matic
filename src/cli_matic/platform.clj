(ns cli-matic.platform
  "
  ## Platform-specific functions for the JVM.

  If running on ClojureScript, we can have a different file for JS.

  BTW, in this NS, we avoid using Spec / Orchestra.

  **DO NOT** define macros in this namespace - see [[cli-matic.platform-macros]]

  "
  (:require [clojure.edn :as edn]))

(defn read-env
  "Reads an environment variable.
  If undefined, returns nil."
  [var]
  (System/getenv var))

(defn read-interactive
  "Reads value from tty.
  If masked? is true masks user input with *"
  [{:keys [prompt masked?]}]
  (let [console (System/console)]
    (print prompt)
    (flush)
    (if masked?
      (apply str (.readPassword console))
      (read-line))))

(defn tty?
  []
  (some? (System/console)))

(defn exit-script
  "Terminates execution with a return value."
  [retval]
  (System/exit retval))

(defn add-shutdown-hook
  "Add a shutdown hook. If `nil`, simply ignores it.

  The shutdown hook is run in a new thread.

  "
  [fnToCallOnShutdown]

  (if (ifn? fnToCallOnShutdown)
    (.addShutdownHook
     (Runtime/getRuntime)
     (Thread. fnToCallOnShutdown))))

(defn slurp-file
  "No slurping in JavaScript. So we have to move this to
  platform."
  [f]
  (slurp f))

;
; Conversions
;

(defn parseInt
  "Converts a string to an integer. "
  [s]
  (Integer/parseInt s))

(defn parseFloat
  "Converts a string to a float."
  [s]
  (Float/parseFloat s))

(defn asDate
  "Converts a string in format yyyy-mm-dd to a
  Date object; if conversion
  fails, returns nil."
  [s]
  (try
    (.parse
     (java.text.SimpleDateFormat. "yyyy-MM-dd") s)
    (catch Throwable t
      nil)))

(defn parseEdn
  "
        Decodes EDN through clojure.edn.
        "
  [edn-in]
  (edn/read-string edn-in))

