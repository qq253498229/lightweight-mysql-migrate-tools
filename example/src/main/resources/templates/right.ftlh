<form class="form-horizontal">
    <div class="form-group">
        <label for="targetHost" class="col-sm-2 control-label">Host</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="targetHost" placeholder="Host"
                   ng-model="parameter.target.host">
        </div>
    </div>
    <div class="form-group">
        <label for="targetPort" class="col-sm-2 control-label">Port</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="targetPort" placeholder="Port"
                   ng-model="parameter.target.port">
        </div>
    </div>
    <div class="form-group">
        <label for="targetUsername" class="col-sm-2 control-label">Username</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="targetUsername" placeholder="Username"
                   ng-model="parameter.target.username">
        </div>
    </div>
    <div class="form-group">
        <label for="targetPassword" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-10">
            <input type="password" class="form-control" id="targetPassword" placeholder="Password"
                   ng-model="parameter.target.password">
        </div>
    </div>
    <div class="form-group">
        <label for="targetName" class="col-sm-2 control-label">数据库名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="targetName" placeholder="Name"
                   ng-model="parameter.target.name">
        </div>
    </div>
</form>