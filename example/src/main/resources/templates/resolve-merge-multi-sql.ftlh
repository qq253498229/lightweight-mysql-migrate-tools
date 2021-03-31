<div>
    <form class="form-horizontal" ng-submit="merge()">

        <div class="form-group">
            <label for="file" class="col-sm-2 control-label">File input</label>
            <div class="col-sm-10">
                <input type="file" id="file" multiple ng-model="files">
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                <button type="submit" class="btn btn-default">合并</button>
            </div>
        </div>

    </form>
</div>