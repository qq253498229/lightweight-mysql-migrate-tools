<form class="form-horizontal">
    <div class="form-group">
        <label for="sourceHost" class="col-sm-2 control-label">Host</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="sourceHost" placeholder="Host"
                   ng-model="parameter.source.host">
        </div>
    </div>
    <div class="form-group">
        <label for="sourcePort" class="col-sm-2 control-label">Port</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="sourcePort" placeholder="Port"
                   ng-model="parameter.source.port">
        </div>
    </div>
    <div class="form-group">
        <label for="sourceUsername" class="col-sm-2 control-label">Username</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="sourceUsername" placeholder="Username"
                   ng-model="parameter.source.username">
        </div>
    </div>
    <div class="form-group">
        <label for="sourcePassword" class="col-sm-2 control-label">Password</label>
        <div class="col-sm-10">
            <input type="password" class="form-control" id="sourcePassword" placeholder="Password"
                   ng-model="parameter.source.password">
        </div>
    </div>
    <div class="form-group">
        <label for="sourceName" class="col-sm-2 control-label">数据库名</label>
        <div class="col-sm-10">
            <input type="text" class="form-control" id="sourceName" placeholder="Name"
                   ng-model="parameter.source.name">
        </div>
    </div>
</form>