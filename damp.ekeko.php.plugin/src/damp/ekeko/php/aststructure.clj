(ns damp.ekeko.php.aststructure
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]]
    [damp.ekeko.php [ast :as ast] [bindings :as bindings]  [astbindings :as astbindings] [phpprojectmodel :as phpprojectmodel]]))



(defn-
  program-for-sourcemodule
  [ism]
  (loop [models (phpprojectmodel/php-project-models)]
    (if 
      (seq models)
      (if-let [program (.getProgramForSourceModule (first models) ism)]
        program
        (recur (rest models)))
      nil)))




(defn 
  declaration-for-element
  [element]
  (let [sourcemodule (program-for-sourcemodule (.getSourceModule element))
        sourcerange (.getSourceRange element)
        sourceoffset (.getOffset sourcerange)]
    (.getElementAt sourcemodule sourceoffset)))
    
  
(defn-
  element-declaration
  [?element ?ast]
  (cl/fresh [?program ?offset]
    (el/v+ ?element)
    (el/equals ?program (program-for-sourcemodule (.getSourceModule ?element)))
    (cl/!= nil ?program)
    (el/equals ?offset (-> ?element .getSourceRange .getOffset))
    (el/equals-without-exception ?ast (.getElementAt ?program ?offset))))






  
