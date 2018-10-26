(ns base64clj.decode
  (:require [base64clj.encode :refer [rev-b64table]]))

(defn u_int->b [i]
  (if (<= i 127)
    i
    (-> i
        (bit-not)
        (bit-and 16r000000ff)
        inc
        -)))

(defn handle-quartet
  ([a b c d] (let [inter-a (bit-shift-left a 18)
                   inter-b (bit-shift-left b 12)
                   inter-c (bit-shift-left c 6)
                   full (bit-or inter-a inter-b inter-c d)]
               [(bit-shift-right (bit-and full 2r111111110000000000000000) 16)
                (bit-shift-right (bit-and full 2r000000001111111100000000) 8)
                (bit-and full 2r000000000000000011111111)]))
  ([a b c] (let [inter-a (bit-shift-left a 12)
                 inter-b (bit-shift-left b 6)
                 full (bit-or inter-a inter-b c)]
             [(bit-shift-right (bit-and full 2r111111110000000000) 10)
              (bit-shift-right (bit-and full 2r000000001111111100) 2)]))
  ([a b]  (let [inter-a (bit-shift-left a 6)
                full (bit-or inter-a b)]
            [(bit-shift-right (bit-and full 2r111111110000) 4)])))

(defn decode [b64s]
  (->> b64s
       (map #(get rev-b64table %))
       (filter (comp not nil?))
       (partition 4 4 nil)
       (mapcat #(apply handle-quartet %))
       (map u_int->b)
       (byte-array)))

(defn decode-to-string [b64s]
  (-> b64s
      (decode)
      (String. "UTF-8")))
