(ns damp.ekeko.php.weightedmethodcount
   (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [astnode :as astnode]] ;todo: extract commonalities to shared namespace
    [damp.ekeko.php
     [McCabecyclomaticnumber :as McCabe]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes 
     ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))


(defn 
  weightedmethodcountclass
  [class]
  (let [methods (astnode/nodeorvalue-offspring|type class :MethodDeclaration)]
  (apply + (mapv (fn [method] (McCabe/CYCLO method))
            methods))))     
             
             
(defn weightedmethodcount
  []  
  (reduce (fn [mapsofar class] (assoc mapsofar class (weightedmethodcountclass class)))
             {} 
             (astnode/asts-for-keyword :ClassDeclaration)))
                 
                      