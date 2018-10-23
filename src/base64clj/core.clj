(ns base64clj.core
  (:require [base64clj.encode :refer [encode encode-string]]
            [base64clj.decode :refer [decode]]
            [base64clj.io :refer [read-into-barray write-to-file]]
            [clojure.java.io :refer [file]]
            [clojure.string :refer [join]])
  (:gen-class))

(defn encode-file [f]
  (let [bs (read-into-barray f)]
    (encode bs)))

(defn decode-to-file [encoding file-path]
  (let [decoded-barr (decode encoding)]
    (write-to-file file-path decoded-barr)))

(defn encode-to-file [in out]
  (->> (encode-file in)
       (spit out)))

(defn decode-from-file [in out]
  (let [b64s (slurp in)]
    (decode-to-file b64s out)))

(defn usage []
  (->> ["A tiny clojure cli tool to base64 encode files."
        ""
        "Usage:"
        ""
        "base64clj encode <input-file> [<output-file>]"
        "base64clj decode <input-file> <output-file>"
        ""]
       (join \newline)))

(defn validate-in-file [action in]
  (if (nil? in)
    {:error? true
     :msg (str "Please provide input file to " action)}
    (if (.exists (file in))
      {:error? false}
      {:error? true
       :msg (str "File " in " does not exist.")})))

(defn validate-input [action in]
  (cond
    (#{"decode"
       "encode"} action) (validate-in-file action in)
    :else {:error? true
           :msg (usage)}))

(defn encoding-case [in out]
  (if (nil? out)
    (let [encoding (encode-file in)]
      (println encoding))
    (encode-to-file in out)))

(defn -main
  [& args]
  (let [[action in out] args]
    (let [{:keys [error? msg]} (validate-input action in)]
      (if error?
        (println msg)
        (case action
          "encode" (encoding-case in out)
          "decode" (decode-from-file in out))))))
