
function randomString(length, chars) 
{
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.round(Math.random() * (chars.length - 1))];
    return result;
}

var phonecatControllers = angular.module('phonecatControllers', []);
 
phonecatControllers.controller('RegressCtrl', ['$scope', '$http', function ($scope, $http) {
}]);

phonecatControllers.controller('FilepickCtrl', ['$scope', '$http', function ($scope, $http) {
   $scope.id = randomString(32, '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');

   $scope.url = 'http://www.igniipotent.com/upload?id=' + $scope.id;
}]);

phonecatControllers.controller('ProcessCtrl', ['$scope', '$routeParams', '$http',
function($scope, $routeParams, $http) {
   $scope.id = $routeParams.id;
   
   $scope.start_poll = function(){
      setInterval ($scope.poll,2000);

      var url = 'http://www.igniipotent.com/test';
      var data = {id : $scope.id, sin_checkbox : document.getElementById('sin_checkbox').checked, cos_checkbox : document.getElementById('cos_checkbox').checked};

      $http.post(url,data);
   };

   $scope.poll = function(){
      $http.get('http://www.igniipotent.com/test?id=' + $scope.id).success(function(data){
      var result_list = {result_list: data};
      $scope.result = result_list;
      });
   };
}]);
