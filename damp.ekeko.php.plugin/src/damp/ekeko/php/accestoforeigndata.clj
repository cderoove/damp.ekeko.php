(ns damp.ekeko.php.accestoforeigndata
 (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [astnode :as astnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [phpprojectmodel :as pm]])
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
          ((let [fieldaccessed (first fieldsaccessed)
                 declaringbinding(.resolveFieldBinding fieldaccessed)
                 typedecelement (.getPHPElement declaringbinding)
                 typedecnode (declaration-for-element typedecelement)]
            (if (= typedecnode class)
              (recur (rest fieldsaccessed)
                     (conj ATFD fieldaccessed))
              (recur (rest fieldsaccessed)
                     ATFD))))))))


;; loops over the project and for each class calculates his acces to foreign data
;; gives back an map with as key the classDeclaration node and as key its ATFD metric
(defn accestoforeigndata
  []
  (let [models (pm/php-project-models)
       model (first models)
       classes (pm/pm-asts|type model org.eclipse.php.internal.core.ast.nodes.ClassDeclaration)]
    (loop[classes classes
          ATFD {}]
      (if (not-empty class)
        (recur (rest classes)
               (assoc ATFD (first classes) (accesstoforeigndataclass (first classes))))
        ATFD))))
  
        


    
                    
                      
                              
        
