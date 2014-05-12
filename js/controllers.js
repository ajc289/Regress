
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

   $scope.form_submit = function(){
      var file_name = document.getElementById('file_selector').value;
      var last_dot = file_name.lastIndexOf(".");
      if (last_dot != -1)
      {
         file_name = file_name.substring(last_dot, file_name.length);
         
         if (file_name != ".csv")
         {
            event.preventDefault();
         }
      }
      else
      {
         event.preventDefault();
      }
   };
}]);

phonecatControllers.controller('ProcessCtrl', ['$scope', '$routeParams', '$http',
function($scope, $routeParams, $http) {
   $scope.id = $routeParams.id;
 
   $scope.start_poll = function(){
      setInterval ($scope.poll,2000);

      var url = 'http://www.igniipotent.com/test';
      var data = {id : $scope.id, square_checkbox : document.getElementById('square_checkbox').checked, square_root_checkbox : document.getElementById('square_root_checkbox').checked,
                  sin_checkbox : document.getElementById('sin_checkbox').checked, cos_checkbox : document.getElementById('cos_checkbox').checked,
                  constant_lower_bound : document.getElementById('constant_lower_bound').value, constant_upper_bound : document.getElementById('constant_upper_bound').value};

      $http.post(url,data);
   };

   $scope.poll = function(){
      $http.get('http://www.igniipotent.com/test?id=' + $scope.id).success(function(data){
      var result_list = {result_list: data};
      $scope.result = result_list;
      });
   };
}]);


phonecatControllers.controller('ProcessExampleCtrl', ['$scope', '$http',
function($scope, $http) {
   document.getElementById('square_checkbox').checked = true;
   document.getElementById('square_root_checkbox').checked = true;
   document.getElementById('sin_checkbox').checked = true;
   document.getElementById('cos_checkbox').checked = true;
   document.getElementById('constant_lower_bound').value = 0.0;
   document.getElementById('constant_upper_bound').value = 1.0;

   $scope.start_poll = function(){
      setInterval ($scope.poll,2000);

      var url = 'http://www.igniipotent.com/test';
      var data = {id : "Rp1woqDWozCp2B5PYjCRX9kuBbk2N27l", square_checkbox : document.getElementById('square_checkbox').checked, square_root_checkbox : document.getElementById('square_root_checkbox').checked,
                  sin_checkbox : document.getElementById('sin_checkbox').checked, cos_checkbox : document.getElementById('cos_checkbox').checked,
                  constant_lower_bound : document.getElementById('constant_lower_bound').value, constant_upper_bound : document.getElementById('constant_upper_bound').value};

      $http.post(url,data);
   };

   $scope.poll = function(){
      $http.get('http://www.igniipotent.com/test?id=' + $scope.id).success(function(data){
      var result_list = {result_list: data};
      $scope.result = result_list;
      });
   };
}]);




