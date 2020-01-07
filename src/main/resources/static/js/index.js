const angular = require('angular');
const trip = require('./ui-bootstrap-tpls.min.js');
const app = angular.module("EmployeeManagement", ["ui.bootstrap"]);
const app2 = angular.module("OrgsManagement", ["ui.bootstrap"]);
const tree1 = angular.module("Tree1", []);
const tree2 = angular.module("Tree2", []);

tree1.controller('Tree1Controller',  ['$scope','$http', function ($scope,$http) {
    $scope.delete = function(data) {
      data.nodes = [];
    };
    $scope.add = function(data) {
      $http({
        method: 'GET',
        url: '/tree1_ref/' + data.id
      }).then(
        function(res) { // success
          data.nodes=(res.data);
      },function(res) { // error
          console.log("Error: " + res.status + " : " + res.data);
      });
    };
    $scope.tree = [];
    _refreshTree1();
    function _success(res) {
    _refreshTree1();
       }
    function _refreshTree1() {
            $http({
                method: 'GET',
                url: '/tree1_ref'
            }).then(
                function(res) { // success
                 $scope.tree=res.data;
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
      }
}]
)

tree2.controller('Tree2Controller',  ['$scope','$http', function ($scope,$http) {
    $scope.delete = function(data) {
      data.nodes = [];
    };
    $scope.add = function(data) {
      $http({
        method: 'GET',
        url: '/tree2_ref/' + data.id
      }).then(
        function(res) { // success
          data.nodes=(res.data);
      },function(res) { // error
          console.log("Error: " + res.status + " : " + res.data);
      });
    };
    $scope.tree = [];
    _refreshTree1();
    function _success(res) {
    _refreshTree1();
       }
    function _refreshTree1() {
            $http({
                method: 'GET',
                url: '/tree2_ref'
            }).then(
                function(res) { // success
                 $scope.tree=res.data;
                },
                function(res) { // error
                    console.log("Error: " + res.status + " : " + res.data);
                }
            );
      }
}]
)

app.controller('EmployeeController',  ['$scope','$http', function ($scope,$http) {
 $scope.page = 1;
 	$scope.pageChanged = function() {
 	  let startPos = ($scope.page - 1) * 3;
 	};
 $scope.numberOfPages=function(){
         return Math.ceil($scope.employees.length/5);
     }
 $scope.employees = [];
 $scope.orgs = [];
    $scope.employeeForm = {
        formId: 1,
        formName: "",
        formOrg: "option-0",
        formSV:"option-0",
        formVis:false
    };

    // Now load the data from server
    _refreshEmployeeData();

    // HTTP POST/PUT methods for add/edit employee
    // Call: http://localhost:8080/employee
    $scope.submitEmployee = function() {
    $scope.employeeForm.formVis=false;
        let method = "";
        let url = "";

        if ($scope.employeeForm.formId == -1) {
            method = "POST";
            url = '/employee';
        } else {
            method = "PUT";
            url = '/employee';
        }

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.employeeForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.createEmployee = function() {
        _clearFormData();
        $scope.employeeForm.formVis=true;
    }

    // HTTP DELETE- delete employee by Id
    // Call: http://localhost:8080/employee/{empId}
    $scope.deleteEmployee = function(employee) {
        $http({
            method: 'DELETE',
            url: '/employee/' + employee.id
        }).then(_success, _error);
    };

    $scope.editEmployee = function(employee) {
        $scope.employeeForm.formId = employee.id;
        $scope.employeeForm.formName = employee.name;
        $scope.employeeForm.formOrg = "option-"+employee.org_id;
        $scope.employeeForm.formSV = "option-"+employee.s_id;
        $scope.employeeForm.formVis=true;
    };

    $scope.org_change = function() {
           $scope.employeeForm.formSV = "option-0";
        };
    function _refreshEmployeeData() {
        $http({
            method: 'GET',
            url: '/employees'
        }).then(
            function(res) { // success
                $scope.employees = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
        $http({
                    method: 'GET',
                    url: '/orgs'
                }).then(
                    function(res) { // success
                        $scope.orgs = res.data;
                    },
                    function(res) { // error
                        console.log("Error: " + res.status + " : " + res.data);
                    }
                );
    }

   $scope.isDel = function(value) {
    for (var i=0; i< $scope.employees.length; i++)
       {
                 if ( $scope.employees[i].s_id == value) return true;
       }
       return false;
   };
    function _success(res) {
        _refreshEmployeeData();
        _clearFormData();
    }

    function _error(res) {
        let data = res.data;
        let status = res.status;
        let header = res.header;
        let config = res.config;
        alert("Error: " + status + ":" + data);
    }

    // Clear the form
    function _clearFormData() {
        $scope.employeeForm.formId = -1;
        $scope.employeeForm.formName = "";
        $scope.employeeForm.formOrg = "option-0",
        $scope.employeeForm.formSV = "option-0"
    }
}]
)



app2.controller('OrgsController',  ['$scope','$http', function ($scope,$http) {
 $scope.page = 1;
 	$scope.pageChanged = function() {
 	  let startPos = ($scope.page - 1) * 3;
 	};
 $scope.numberOfPages=function(){
         return Math.ceil($scope.orgs.length/5);
     }
 $scope.orgs = [];
    $scope.orgForm = {
        formId: 1,
        formName: "",
        formParent:"option-0",
        formVis:false
    };

    // Now load the data from server
    _refreshOrgsData();

    // HTTP POST/PUT methods for add/edit employee
    // Call: http://localhost:8080/employee
    $scope.submitOrg = function() {
    $scope.orgForm.formVis=false;
        let method = "";
        let url = "";

        if ($scope.orgForm.formId == -1) {
            method = "POST";
            url = '/org';
        } else {
            method = "PUT";
            url = '/org';
        }

        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.orgForm),
            headers: {
                'Content-Type': 'application/json'
            }
        }).then(_success, _error);
    };

    $scope.createOrg = function() {
        _clearFormData();
        $scope.orgForm.formVis=true;
    }

    // HTTP DELETE- delete employee by Id
    // Call: http://localhost:8080/employee/{empId}
    $scope.deleteOrg = function(org) {
        $http({
            method: 'DELETE',
            url: '/org/' + org.id
        }).then(_success, _error);
    };

    $scope.editOrg = function(org) {
        $scope.orgForm.formId = org.id;
        $scope.orgForm.formName = org.name;
        $scope.orgForm.formParent = "option-"+org.p_id;
        $scope.orgForm.formVis=true;
    };
    function _refreshOrgsData() {
        $http({
            method: 'GET',
            url: '/org'
        }).then(
            function(res) { // success
                $scope.orgs = res.data;
            },
            function(res) { // error
                console.log("Error: " + res.status + " : " + res.data);
            }
        );
    }

   $scope.isDel = function(value) {
    for (var i=0; i< $scope.orgs.length; i++)
       {
                 if ( $scope.orgs[i].p_id == value) return true;
       }
       return false;
   };
    function _success(res) {
        _refreshOrgsData();
        _clearFormData();
    }

    function _error(res) {
        let data = res.data;
        let status = res.status;
        let header = res.header;
        let config = res.config;
        alert("Error: " + status + ":" + data);
    }

    // Clear the form
    function _clearFormData() {
        $scope.orgForm.formId = -1;
        $scope.orgForm.formName = "";
        $scope.orgForm.formParent = "option-0"
    }
}]
)