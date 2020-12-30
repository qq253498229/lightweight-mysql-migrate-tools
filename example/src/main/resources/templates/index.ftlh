<!doctype html>
<html lang="en" ng-app="app">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>MySQL迁移工具示例</title>
    <link href="https://cdn.bootcdn.net/ajax/libs/twitter-bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
</head>
<body ng-controller="ctrl">

<div class="container-fluid" ng-cloak>
    <div class="row">
        <div class="col-md-5">
            <#include "./left.ftlh"/>
        </div>
        <div class="col-md-2">
            <#include "./center.ftlh"/>
        </div>
        <div class="col-md-5">
            <#include "./right.ftlh"/>
        </div>
    </div>

    <div class="row">
        <#include "./content.ftlh"/>
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