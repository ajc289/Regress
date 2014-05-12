var phonecatApp = angular.module('phonecatApp', ['ngRoute','phonecatControllers']);

phonecatApp.config(['$routeProvider',
   function ($routeProvider){
      $routeProvider.
         when('/regress',{templateUrl: 'partials/regress.html', controller: 'RegressCtrl'}).
         when('/filepick',{templateUrl: 'partials/filepick.html', controller: 'FilepickCtrl'}).
         when('/filepick_size_error',{templateUrl: 'partials/filepick_size_error.html', controller: 'FilepickCtrl'}).
         when('/process/:id',{templateUrl: 'partials/process.html', controller: 'ProcessCtrl'}).
         when('/example',{templateUrl: 'partials/process_example.html', controller: 'ProcessExampleCtrl'}).
         otherwise({redirectTo: '/regress'});
      }]);
