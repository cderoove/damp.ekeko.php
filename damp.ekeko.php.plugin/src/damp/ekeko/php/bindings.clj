(ns damp.ekeko.php.bindings
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]]
    [damp.ekeko.php [phpprojectmodel :as phpprojectmodel]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes IBinding]))
   
(defn 
  binding-element
  [?ibinding ?imodelelement]
  (cl/all
    (el/v+ ?ibinding)
    (cl/!= nil ?imodelelement)
    (el/equals ?imodelelement (.getPHPElement ^IBinding ?ibinding))))


