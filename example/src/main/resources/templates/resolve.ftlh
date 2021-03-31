<!doctype html>
<html lang="en" ng-app="app">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>MySQL处理</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body ng-controller="ctrl">

<div class="container-fluid" ng-cloak>
    <div style="margin-top: 15px;margin-bottom: 15px;">
        <ul class="nav nav-tabs">
            <li role="presentation" ng-class="{'active':tab=='tab1'}" ng-click="changeTab('tab1')">
                <a href="#">执行SQL</a>
            </li>
            <li role="presentation" ng-class="{'active':tab=='tab2'}" ng-click="changeTab('tab2')">
                <a href="#">导出SQL</a>
            </li>
            <li role="presentation" ng-class="{'active':tab=='tab3'}" ng-click="changeTab('tab3')">
                <a href="#">合并多个SQL文件</a>
            </li>
        </ul>
    </div>
    <div ng-if="tab == 'tab1'">
        <#include "./resolve-execute-sql.ftlh"/>
    </div>
    <div ng-if="tab == 'tab2'">
        <#include "./resolve-export-sql.ftlh"/>
    </div>
    <div ng-if="tab == 'tab3'">
        <#include "./resolve-merge-multi-sql.ftlh"/>
    </div>
</div>


<script src="https://cdn.bootcdn.net/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/angular.js/1.8.2/angular.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/angular-sanitize/1.8.2/angular-sanitize.min.js"></script>
<script src="https://cdn.bootcdn.net/ajax/libs/sql-formatter/2.3.3/sql-formatter.min.js"></script>
<script src="js.js"></script>
</body>
</html>