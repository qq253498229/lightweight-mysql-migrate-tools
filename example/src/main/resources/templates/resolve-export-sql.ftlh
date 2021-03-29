<div>
    <form class="form-horizontal" ng-submit="export()">
        <div class="form-group">
            <label for="host" class="col-sm-2 control-label">host</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="host" placeholder="host" ng-model="info.host">
            </div>
        </div>
        <div class="form-group">
            <label for="port" class="col-sm-2 control-label">port</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="port" placeholder="port" ng-model="info.port">
            </div>
        </div>
        <div class="form-group">
            <label for="username" class="col-sm-2 control-label">username</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="username" placeholder="username" ng-model="info.username">
            </div>
        </div>
        <div class="form-group">
            <label for="password" class="col-sm-2 control-label">password</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="password" placeholder="password" ng-model="info.password">
            </div>
        </div>
        <div class="form-group">
            <label for="name" class="col-sm-2 control-label">数据库名</label>
            <div class="col-sm-10">
                <input type="text" class="form-control" id="name" placeholder="name" ng-model="info.name">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">导出</button>
            </div>
        </div>
    </form>
</div>