const app = angular.module('app', ['ngSanitize'])
app.filter('format', function () {
    return function (inputString) {
        let result = sqlFormatter.format(inputString);
        result = result.replaceAll('\n', '<br>');
        console.log(result);
        return result;
    }
})
app.filter('title', function () {
    return function (inputString) {
        if (!inputString) {
            inputString = 'sql'
        }
        if (inputString.length > 200) {
            inputString = inputString.substr(0, 100) + "...";
        }
        return inputString;
    }
})
app.controller('ctrl', function ($scope, $http) {
    $scope.parameter = {
        source: {
            host: '',
            port: '',
            username: '',
            password: ''
        },
        target: {
            host: '',
            port: '',
            username: '',
            password: ''
        }
    }
    $scope.list = []
    $scope.onInit = function () {
        if (!!localStorage.getItem('parameter')) {
            $scope.parameter = JSON.parse(localStorage.getItem('parameter'))
        }
    }
    $scope.onInit()
    $scope.diff = function () {
        localStorage.setItem('parameter', JSON.stringify($scope.parameter))
        $http.post('/diff', $scope.parameter).then(res => {
            $scope.list = res.data;
        })
    }
    $scope.update = function () {
        alert('更新太恐怖了，所以没实现。。。。')
    }
})