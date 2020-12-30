<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true" style="margin-top: 20px">
    <div class="panel panel-default" ng-repeat="obj in list track by $index">

        <div class="panel-heading" role="tab" id="heading{{$index}}">
            <h4 class="panel-title">
                <a role="button" data-toggle="collapse" data-parent="#accordion" href="#collapse{{$index}}"
                   aria-expanded="true" aria-controls="collapse{{$index}}">
                    {{obj | title}}
                </a>
            </h4>
        </div>
        <div id="collapse{{$index}}" class="panel-collapse collapse" ng-class="{'in': $index == 0 }" role="tabpanel"
             aria-labelledby="heading{{$index}}">
            <div class="panel-body" ng-bind-html="obj | format">
            </div>
        </div>

    </div>

</div>