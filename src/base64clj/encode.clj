(ns base64clj.encode)


(defonce b64table
  "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/")

(defonce rev-b64table
  (zipmap b64table (range 64)))

(defn strip-newlines [s]
  (filter #(not= % \newline) s))

(defn str->barr [s]
  (.getBytes s "UTF-8"))

(defn seq->str [s]
  (apply str s))

(defn b->u_int [b]
  (Byte/toUnsignedInt b))

(defn handle-ending [out carry-num carry-bits]
  (if (= carry-bits 4)
    (conj out (bit-shift-left carry-num 2) 2r1000000)
    (conj out (bit-shift-left carry-num 4) 2r1000000 2r1000000)))

(defn extract-sextets [bs]
  (loop [bytes bs
         out []
         carry-num nil
         carry-bits 0]
    (if (not-empty bytes)
      (let [b (first bytes)]
        (cond
          (= 2 carry-bits)
          (recur (rest bytes)
                 (conj out (bit-or (bit-shift-left carry-num 4)
                                   (bit-shift-right b 4)))
                 (bit-and b 15)
                 4)
          (= 4 carry-bits)
          (recur (rest bytes)
                 (->> (concat out [(bit-or (bit-shift-left carry-num 2)
                                           (bit-shift-right b 6))
                                   (bit-and b 63)])
                      (into []))
                 nil
                 0)
          :else (recur (rest bytes)
                       (conj out (bit-shift-right b 2))
                       (bit-and b 3)
                       2)))
      (if-not (zero? carry-bits)
        (handle-ending out carry-num carry-bits)
        out))))

(defn encode [#^bytes bs]
  (->> bs
       (map b->u_int)
       (partition-all 3)
       (mapcat extract-sextets)
       (map #(get b64table % \=))
       seq->str))

(defn encode-string [s]
  (-> s
      str->barr
      encode))
