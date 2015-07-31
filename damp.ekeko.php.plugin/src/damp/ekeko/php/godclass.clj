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
  (filterv
    (fn [keyvaluepair]
      (> (val keyvaluepair) (:few thresholds/thresholds)))
    (ATFD/accesstoforeigndata)))

(defn
  filteredWMC
  []
   (filterv
    (fn [keyvaluepair]
      (>= (val keyvaluepair) (:veryhigh thresholds/thresholds)))
    (WMC/weightedmethodcount)))

(defn
  filteredTCC
   []
   (filterv
    (fn [keyvaluepair]
      (< (val keyvaluepair) (:onethird thresholds/thresholds)))
    (TCC/tightclasscohesion)))

(defn 
 godclass
 []
 (metrics/mapintersection (filteredTCC) (filteredWMC) (filteredATFD)))
