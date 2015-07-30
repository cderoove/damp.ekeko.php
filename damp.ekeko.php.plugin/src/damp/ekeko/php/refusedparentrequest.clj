(ns damp.ekeko.php.refusedparentrequest
    (:require 
     [damp.ekeko.php
     [averagemethodweight :as AMW]] 
     [damp.ekeko.php
     [weightedmethodcount :as WMC]]
     [damp.ekeko.php
     [numberofprotectedmembers :as NProtM]]
     [damp.ekeko.php
     [numberofmethods :as NOM]]
     [damp.ekeko.php
     [baseclassusageratio :as BUR]]
     [damp.ekeko.php
     [baseclassoverridingratio :as BOvR]]
     [damp.ekeko.php
     [thresholds :as thresholds]]
     [clojure.set]))

(defn
  filteredNProtM
   (filterv
     (fn [keyvaluepair]
       (> (val keyvaluepair) (:few thresholds/thresholds)))
     (NProtM/numberofprotectedmembers)))

(defn
  filteredBUR
  []
   (filterv
   (fn [keyvaluepair]
     (< (val keyvaluepair) (:onethird thresholds/thresholds)))
   (BUR/baseclassusageratio)))

(defn
  filteredBOvR
  []
  (filterv
   (fn [keyvaluepair]
     (< (val keyvaluepair) (:onethird thresholds/thresholds)))
   (BOvR/baseclassoverridingratio)))

(defn
  filteredAMW
  []
  (filterv
   (fn [keyvaluepair]
     (> (val keyvaluepair) (:averageAMW thresholds/thresholds)))
   (AMW/averagemethodweight)))

(defn
  filteredNOM
  []
  (filterv
   (fn [keyvaluepair]
     (> (val keyvaluepair) (:averageNOM thresholds/thresholds)))
   (NOM/numberofmethods)))

(defn 
  filteredWMC
  []
  (filterv
   (fn [keyvaluepair]
     (> (val keyvaluepair) (:averageWMC thresholds/thresholds)))
   (WMC/weightedmethodcount)))

(defn
  childclassistosimple
  []
  (clojure.set/intersection (filteredNOM) (clojure.set/union (filteredAMW) (filteredWMC))))

(defn 
  childclassignoresrequest
  []
  (clojure.set/union (clojure.set/intersection (filteredNProtM) (filteredBUR)) (filteredBOvR)))

(def 
  refusedparentrequest
  []
  (clojure.set/intersection (childclassignoresrequest) (childclassistosimple)))