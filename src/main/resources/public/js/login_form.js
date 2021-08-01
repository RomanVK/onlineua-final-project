angular.module("login_form",[])
    .controller("AppCtrl", function ($scope, $http) {
        $scope.auth = {};
        $scope.sendForm = function(auth){
            $http({
                method: "POST",
                url: "/login",
                data: $.param(auth),
                headers: { "Content-Type" : "application/x-www-form-urlencoded" }
            }).then(
                function(data) {
                    // TODO do i18n
                    window.alert("Доступ разрешен");
                },
                function(error) {
                    // TODO do i18n
                    window.alert("Доступ запрещен");
                }
            );
        }
    });