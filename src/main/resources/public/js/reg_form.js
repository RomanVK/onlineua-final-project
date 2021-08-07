angular.module("registration_form",[])
    .controller("AppCtrl", function ($scope, $http) {
        $scope.auth = {};
        $scope.sendForm = function(auth){
            $http({
                method: "POST",
                url: "/registration_form",
                data: $.param(auth),
                headers: { "Content-Type" : "application/x-www-form-urlencoded" }
            }).then(
                function(data) {
                    //TODO make i18n
                    window.alert("Успешно зарегистрирован");
                },
                function(error) {
                    //TODO make i18n
                    window.alert("При регистрации произошла ошибка");
                }
            );
        }
    });