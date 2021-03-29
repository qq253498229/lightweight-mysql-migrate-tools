const app = angular.module('app', ['ngSanitize'])
app.filter('format', function () {
    return function (inputString) {
        let result = sqlFormatter.format(inputString);
        result = result.replaceAll('\n', '<br>');
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
    $scope.info = {}
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

    $scope.diff = function () {
        localStorage.setItem('parameter', JSON.stringify($scope.parameter))
        $http.post('/diff', $scope.parameter).then(res => {
            $scope.list = res.data;
        })
    }
    $scope.showLeftSql = function () {
        $http.post('/showSql', $scope.parameter.source).then(res => {
            $scope.list = res.data;
        })
    }
    $scope.showRightSql = function () {
        $http.post('/showSql', $scope.parameter.target).then(res => {
            $scope.list = res.data;
        })
    }
    $scope.update = function () {
        alert('更新太恐怖了，所以没实现。。。。')
    }
    $scope.loadTab = function () {
        let tab = localStorage.getItem('tab')
        if (!tab) {
            tab = 'tab2'
        }
        $scope.tab = tab;
    }
    $scope.changeTab = function (name) {
        localStorage.setItem('tab', name)
        $scope.tab = name;
    }
    $scope.loadExport = function () {
        let exportString = localStorage.getItem('export')
        if (!!exportString) {
            $scope.info = JSON.parse(exportString)
        }
    }
    $scope.export = function () {
        localStorage.setItem('export', JSON.stringify($scope.info))
        $http.post('/export', $scope.info, {responseType: 'arraybuffer'}).then(function (res) {
            const blob = new Blob([res.data], {type: 'text/x-sql'});
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'export.sql';
            a.click();
            window.URL.revokeObjectURL(url);
        })
    }

    $scope.onInit = function () {
        if (!!localStorage.getItem('parameter')) {
            $scope.parameter = JSON.parse(localStorage.getItem('parameter'))
        }
        $scope.loadTab()
        $scope.loadExport()
    }
    $scope.onInit()
})