(ns 
  damp.ekeko.php.phpprojectmodel
  (:require 
    [damp.ekeko
     [ekekomodel :as ekekomodel]])
  (:import 
    [damp.ekeko.php.plugin PHPProjectModel]))

(defn
  php-project-models
  []
  (filter (fn [model] (instance? PHPProjectModel model))
          (ekekomodel/queried-project-models)))

(defn
  pm-asts|type
  [^PHPProjectModel projectmodel ^java.lang.Class type]
  (.getNodes projectmodel type))
  
(defn
  pm-asts
  [^PHPProjectModel projectmodel]
  (.getNodes projectmodel))
  



