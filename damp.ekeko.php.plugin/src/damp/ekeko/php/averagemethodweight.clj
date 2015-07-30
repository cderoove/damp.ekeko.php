(ns damp.ekeko.php.averagemethodweight
   (:require 
     [damp.ekeko.php
     [astnode :as astnode]]
    [damp.ekeko.php
     [weightedmethodcount :as WMC]] 
    [damp.ekeko.php
     [numberofmethods :as NOM ]]))

(defn
  averagemethodweightclass
  [class]
  (float (/ (WMC/weightedmethodcountclass class) (NOM/numberofmethodsclass class))))

(defn 
  averagemethodweight
  []
  (reduce (fn [mapsofar class] (assoc mapsofar class (averagemethodweightclass class)))
             {} 
             (astnode/classdeclarations)))

