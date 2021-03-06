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
    $scope.menu = [
        {name: '对比数据库', key: 'tab1', filename: './diff.ftlh'},
        {name: '执行SQL', key: 'tab2', filename: './resolve-execute-sql.ftlh'},
        {name: '导出SQL', key: 'tab3', filename: './resolve-export-sql.ftlh'},
        {name: '合并多个SQL文件', key: 'tab4', filename: './resolve-merge-multi-sql.ftlh'},
    ];
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
    $scope.exportDifference = function () {
        localStorage.setItem('parameter', JSON.stringify($scope.parameter))
        console.log($scope.parameter)
        $http.post('/exportDifference', $scope.parameter).then(res => {
            console.log(res)
            const blob = new Blob([res.data], {type: 'text/x-sql'});
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'diff.sql';
            a.click();
            window.URL.revokeObjectURL(url);
        })
    }
    $scope.update = function () {
        alert('更新太恐怖了，所以没实现。。。。')
    }
    $scope.loadTab = function () {
        let tab = localStorage.getItem('tab')
        if (!tab) {
            tab = 'tab1'
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
    $scope.merge = function () {
        const file = document.getElementById("file").files;
        const data = new FormData();
        for (let i = 0; i < file.length; i++) {
            data.append('files', file[i])
        }
        const config = {
            headers: {'Content-Type': undefined}
        };
        $http.post('/merge', data, config).then(res => {
            const blob = new Blob([res.data], {type: 'text/x-sql'});
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'merge.sql';
            a.click();
            window.URL.revokeObjectURL(url);
        })
    }
    $scope.execute = function () {
        const file = document.getElementById("file").files;
        const data = new FormData();
        for (let i = 0; i < file.length; i++) {
            data.append('files', file[i])
        }
        data.append('host', $scope.info.host)
        data.append('port', $scope.info.port)
        data.append('username', $scope.info.username)
        data.append('password', $scope.info.password)
        data.append('name', $scope.info.name)
        const config = {
            headers: {'Content-Type': undefined},
        };
        $http.post('/execute', data, config).then(() => {
            alert('执行成功');
        }, () => {
            alert('执行失败');
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