(ns damp.ekeko.php.accestoforeigndata
 (:require 
    [damp.ekeko.php
     [astnode :as astnode]]
    [damp.ekeko.php
     [metrics :as metrics]]
    [damp.ekeko.php
     [aststructure :as aststruct]]
    [damp.ekeko.php
     [astbindings :as astbindings]]
    [damp.ekeko.php
     [bindings :as bindings]]))


(defn 
  accesstoforeigndataclass
  [class] 
  (let [fieldsaccessed (astnode/nodeorvalue-offspring|type class :FieldAccess )]
  (loop [ fieldsaccessed fieldsaccessed
         ATFD []]
    (if (empty? fieldsaccessed)
      (count ATFD)
      (let [fieldaccessed (first fieldsaccessed)
            decnode (metrics/declaringnode fieldaccessed)]
         (if (or (= decnode class) (nil? decnode))
           (recur (rest fieldsaccessed)
                 ATFD)
           (recur (rest fieldsaccessed)
                  (conj ATFD fieldaccessed))))))))


;; loops over the project and for each class calculates his acces to foreign data
;; gives back an map with as key the classDeclaration node and as key its ATFD metric


(defn accesstoforeigndata
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (accesstoforeigndataclass class)))
             {} 
             (astnode/classdeclarations)))
                 
                      
                              
        
