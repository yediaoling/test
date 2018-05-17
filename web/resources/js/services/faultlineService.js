/**
 * Created by yediaoling on 2016/5/4.
 */

appPW.factory('faultlineService',function($http){

    var service = {};
    service.flag = 0;
    service.ID = "";
    service.subStation = "";
    service.timer = "";
    service.setID  = function(ID){
        service.ID = ID;
    };
    service.setSubStation  = function(ID){
        service.subStation = ID;
    };
    service.getID = function(){
        return service.ID;
    };
    service.getSubStation = function(){
        return service.subStation;
    };
    service.settimer  = function(timer){
        service.timer = timer;
    };
    service.gettimer = function(){
        return service.timer;
    };


    service.setflag  = function(flag){
        service.flag = flag;
    };
    service.getflag = function(){
        return service.flag;
    };

    return service;
});