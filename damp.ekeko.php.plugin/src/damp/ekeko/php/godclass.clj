(ns damp.ekeko.php.godclass
     (:require 
     [damp.ekeko.php
     [accestoforeigndata :as ATFD]] 
     [damp.ekeko.php
     [weightedmethodcount :as WMC]]
     [damp.ekeko.php
     [tightclasscohesion :as TCC]]
     [damp.ekeko.php
     [thresholds :as thresholds]]
     [damp.ekeko.php
     [metrics :as metrics]]
     [clojure.set]))

(defn
 filteredATFD
  []
  (filter
    (fn [keyvaluepair]
      (> (val keyvaluepair) (:few thresholds/thresholds)))
    (ATFD/accesstoforeigndata)))

(defn
  filteredWMC
  []
   (filter
    (fn [keyvaluepair]
      (>= (val keyvaluepair) (:veryhigh thresholds/thresholds)))
    (WMC/weightedmethodcount)))

(defn
  filteredTCC
   []
   (filter
    (fn [keyvaluepair]
     (and (< (val keyvaluepair) (:onethird thresholds/thresholds))  (>(val keyvaluepair) 0)))
    (TCC/tightclasscohesion)))



(defn lazyseqkeys
  [lazyseq]  
  (loop [lazyseq lazyseq
         keys ()]
    (if (empty? lazyseq)
      keys
     (recur (rest lazyseq)
           (cons (first (first lazyseq)) keys)))))
                 
                      
(defn 
 godclass
 []
 (clojure.set/intersection (set (lazyseqkeys (filteredATFD))) (set (lazyseqkeys (filteredWMC))) (set (lazyseqkeys (filteredTCC))))) 


