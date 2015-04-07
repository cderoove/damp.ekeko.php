(ns damp.ekeko.php.aststructure
  (:refer-clojure :exclude [== type])
  (:require [clojure.core [logic :as cl]])
  (:require 
    [damp.ekeko [logic :as el]]
    [damp.ekeko.php [ast :as ast] [structure :as structure] [bindings :as bindings]  [astbindings :as astbindings] [phpprojectmodel :as phpprojectmodel]]))



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

(defn-
  element-declaration
  [?element ?ast]
  (cl/fresh [?program ?offset]
    (el/v+ ?element)
    (el/equals ?program (program-for-sourcemodule (.getSourceModule ?element)))
    (cl/!= nil ?program)
    (el/equals ?offset (-> ?element .getSourceRange .getOffset))
    (el/equals-without-exception ?ast (.getElementAt ?program ?offset))))



(defn
  ast|expression-type 
  [?key ?ast ?type]
  (cl/fresh [?tbinding]
           (astbindings/ast|expression-binding|type ?key ?ast ?tbinding)
           (bindings/binding-element ?tbinding ?type)))


(defn 
  ast|typedeclaration-type
  [?ast ?type]
  (cl/conda [(el/v- ?type)
             (cl/fresh [?binding]
                      (astbindings/ast|typedeclaration-binding|type ?ast ?binding)
                      (bindings/binding-element ?binding ?type))]
           [(el/v+ ?type)
            (cl/!= nil ?type)
            (element-declaration ?type ?ast)
            (cl/!= nil ?ast)]))
  
