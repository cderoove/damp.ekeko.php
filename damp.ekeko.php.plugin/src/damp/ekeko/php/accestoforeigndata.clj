(ns damp.ekeko.php.accestoforeigndata
 (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [astnode :as astnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [phpprojectmodel :as pm]]
    [damp.ekeko.php
     [aststructure :as aststruct]]
    [damp.ekeko.php
     [astbindings :as astbindings]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes 
     ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))


(defn 
  accesstoforeigndataclass
  [class] 
  (let [fieldsaccessed (astnode/nodeorvalue-offspring|type class :FieldAccess )]
  (loop [ fieldsaccessed fieldsaccessed
         ATFD []]
    (if (empty? fieldsaccessed)
      (count ATFD)
      (let [fieldaccessed (first fieldsaccessed)
            decnode (if-let [fieldbinding (.resolveFieldBinding fieldaccessed)]
                      (if-let [declaringbinding (.getDeclaringClass fieldbinding)]
                        (if-let [typedecelement (.getPHPElement declaringbinding)]
                           (if-let [decnode (aststruct/declaration-for-element typedecelement)]
                            decnode
                            nil))))]
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
             (astnode/asts-for-keyword :ClassDeclaration)))
                 
                      
                              
        
