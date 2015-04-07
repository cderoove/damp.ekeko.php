(ns 
  damp.ekeko.php.astnode
  (:require 
    [damp.util.interop]
    [damp.ekeko.php
     [phpprojectmodel :as pm]])
  (:import 
    [org.eclipse.php.internal.core.ast.nodes 
     ASTNode StructuralPropertyDescriptor ChildListPropertyDescriptor ChildPropertyDescriptor SimplePropertyDescriptor]))

;; Auxiliary functions


(defn
  keyword-for-class
  [class]
  (keyword (.getSimpleName ^Class class)))

(defn
  class-for-qualifiedname 
  [name]
  (Class/forName name))

(def
  class-for-packagename-simplename
  (memoize 
    (fn [prefix name]
      (class-for-qualifiedname (str prefix "." name)))))
  
(defn
  class-for-keyword-package
  [key packagename]
  (class-for-packagename-simplename packagename (name key)))


(def
  packagename|pdt
  "org.eclipse.php.internal.core.ast.nodes")


(defn
  class-for-keyword
  [key]
  (class-for-keyword-package key packagename|pdt))

(defn
  keyword-for-class-of
  [node]
  (keyword-for-class (class node)))
 

(defn 
  ast?
  [x]
  (instance? ASTNode x))

(defn
  ast-of-keyword?
  [node key]
  (and 
    (ast? node)
    (instance? (class-for-keyword key) node)))


(defn
  asts-for-keyword
  [key]
  (let [clazz (class-for-keyword key)]
    (mapcat 
      (fn [projectmodel]  (pm/pm-asts|type projectmodel clazz))
      (pm/php-project-models))))

(defn
  asts
  []
  (mapcat 
    (fn [projectmodel] (pm/pm-asts projectmodel))
    (pm/php-project-models)))


;; Property descriptors
;; --------------------

(defn property-descriptor-id [^StructuralPropertyDescriptor p]
  (.getId p))

(defn property-descriptor-simple? [^StructuralPropertyDescriptor p]
  (.isSimpleProperty p))

(defn property-descriptor-list? [^StructuralPropertyDescriptor p]
  (.isChildListProperty p))

(defn property-descriptor-child? [^StructuralPropertyDescriptor p]
  (.isChildProperty p))

(defn ^Class property-descriptor-owner-node-class [^StructuralPropertyDescriptor p]
  (.getNodeClass p))

(defn property-descriptor-child-node-class [^ChildPropertyDescriptor p]
  (.getChildType p))

(defn property-descriptor-element-node-class [^ChildListPropertyDescriptor p]
  (.getElementType p))

(defn property-descriptor-value-class [^SimplePropertyDescriptor p]
  (.getValueType p))

(defn ekeko-keyword-for-property-descriptor [p] 
  (keyword (property-descriptor-id p)))

(defn node-property-value [^ASTNode n ^StructuralPropertyDescriptor p]
  (.getStructuralProperty n p))

(defn 
  nodeclass-property-descriptors 
  [^Class cls]
  (try 
    (damp.util.interop/get-invisible-field cls "PROPERTY_DESCRIPTORS" nil)
    (catch java.lang.NoSuchFieldException e 
       (damp.util.interop/get-invisible-field cls "PROPERTY_DESCRIPTORS_PHP5" nil))))
  

(defn-
  valuekind
  [property]
  (cond
    (property-descriptor-list? property) 
    :list
    (property-descriptor-simple? property)
    :primitive))

(defn
  make-value
  [owner property]
  [(valuekind property) owner property])

(defn
  make-value|nil
  [owner property] 
  [:nil owner property])

  
(defn
  value?
  [x]
  (vector? x))


(defn-
  valuekind?
  [valuekeyword value]
  (= valuekeyword (nth value 0))) 


(defn
  value-property
  [x]
  (nth x 2))

(defn
  value-owner
  [x]
  (nth x 1))


(defn
  lstvalue?
  [x]
  (and 
    (value? x)
    (valuekind? :list x)))

(def 
  value|list?
  lstvalue?)

(defn
  nilvalue?
  [x]
  (and
    (value? x)
    (valuekind? :nil x)))

(def
  value|null?
  nilvalue?)



(defn
  primitivevalue?
  [x]
  (and
    (value? x)
    (valuekind? :primitive x)))

(def 
  value|primitive?
  primitivevalue?)
  
(defn
  value-unwrapped
  [value]
  (if-let [[valuekind owner property] value]
    (node-property-value owner property) ;;not the reified variant since we need the most primitive one
    ))      
              
(def 
  node-ekeko-properties-for-class
  (memoize
    (fn [^Class nc]
      (let [descriptors (nodeclass-property-descriptors nc)]
        (zipmap (map (fn [^StructuralPropertyDescriptor p] 
                       (keyword (property-descriptor-id p)))
                     descriptors)
                (map (fn [^StructuralPropertyDescriptor p]
                       (if 
                         (property-descriptor-child? p)
                         (fn [n]
                           (let [value (node-property-value n p)]
                             (if 
                               (ast? value)
                               value
                               (make-value|nil n p))))
                         (fn [n]
                           (make-value n p))))
                     descriptors)
                )))))

(defn
  node-ekeko-properties
  [node]
  (node-ekeko-properties-for-class (class node)))



(defprotocol 
  IHasOwner
  (owner [n-or-wrapper]))
          
(extend-protocol
  IHasOwner
  ASTNode 
  (owner [this] (.getParent ^ASTNode this))
  ;PropertyValueWrapper ;TODO: switch to record as soon as core.logic no longer reifies records as maps
  clojure.lang.PersistentVector
  (owner [this] (value-owner this)))

(defprotocol 
  IValueOfProperty
  ;PropertyValueWrapper ;TODO: switch to record as soon as core.logic no longer reifies records as maps  
  (owner-property [n-or-wrapper]))

(extend-protocol
  IValueOfProperty
  ASTNode 
  (owner-property [this] (.getLocationInParent ^ASTNode this))
  ;PropertyValueWrapper ;TODO: switch to record as soon as core.logic no longer reifies records as maps
  clojure.lang.PersistentVector
  (owner-property [this] (value-property this)))
