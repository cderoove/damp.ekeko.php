(ns damp.ekeko.php.bindings
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes IBinding]))
   
(defn 
  binding|element
  [binding]
   (.getPHPElement binding))

(defn
  binding|declaringclass
  [binding]
  (.getDeclaringClass binding))

               