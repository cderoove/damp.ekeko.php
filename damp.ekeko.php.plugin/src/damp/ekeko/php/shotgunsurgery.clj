(ns damp.ekeko.php.shotgunsurgery
   (:require 
     [damp.ekeko.php
     [changingmethods :as CM]] 
     [damp.ekeko.php
     [changingclasses :as CC]]
     [damp.ekeko.php
     [thresholds :as thresholds]]
     [clojure.set]))

(defn
  filteredchangingmethods
  []
  (filterv
    (fn [keyvaluepair]
      (> (val keyvaluepair) (:shortmemcap thresholds/thresholds)))
    (CM/changingmethods)))

(defn
  filteredchangingclasses
  []
  (filterv
    (fn [keyvaluepair]
      (> (val keyvaluepair) (:many thresholds/thresholds)))
    (CC/changingclasses)))

(defn 
  shotgunsurgery
  []
  (clojure.set/intersection (filteredchangingclasses) (filteredchangingmethods)))


   