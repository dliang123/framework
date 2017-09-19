(function () {

    function resultCheck(result){
        if( !result.success ){

            alert( result.message );
            if( result.code == -10 ){
                window.top.location = "../login.html"
            }
            return false;
        }
        return true;
    }

    $.resultConvert = function(result){
        if( resultCheck(result) ){
            return result.data;
        }
        return null;
    }


    function resultConvert( e, settings, json, xhr  ){
        if( resultCheck(json) ){
            $.extend(json,json.data);
        }
    }


    $.extend( $.fn.dataTable.defaults ,{ processing:true, serverSide:true , searching:false,

        initComplete:function(){
            var $nTableWrapper=$(arguments[0].nTableWrapper),
                $info = $nTableWrapper.find(".dataTables_info").css({"display":"inline-block","padding-left":"10px","padding-right":"10px"}),
                $length = $nTableWrapper.find(".dataTables_length").css({"display":"inline-block","padding-left":"10px","padding-right":"10px"});
            $info.after($length);
        },
        //pagingType: "full_numbers",
        //dom: 'rt<"row"<"col-sm-2"i><"col-sm-2"l><"col-sm-8"p>><"clear">',
        language:{
            "processing": "处理中...",
            "lengthMenu": "显示 _MENU_ 项结果",
            "zeroRecords": "没有匹配结果",
            "info": "第 _START_ 至 _END_ 项结果,共 _TOTAL_ 项结果",
            "infoEmpty": "暂无数据",
            "infoFiltered": "(由 _MAX_ 项结果过滤)",
            "infoPostFix": "",
            "search": "搜索:",
            //"url": "",
            "emptyTable": "没有找到数据",
            "loadingRecords": "载入中...",
            "infoThousands": ",",
            "paginate": {
                "first": "首页",
                "previous": "上页",
                "next": "下页",
                "last": "末页"
            },
            "aria": {
                "sortAscending": ": 以升序排列此列",
                "sortDescending": ": 以降序排列此列"
            }
        }
    } );


    var tableClass = "_datatable",
        tableUrlAttrName = "_url",
        tableAttrName = "t-s",
        columnDataName = "_data",
        columnAttrName = "c-s",
        colvisClass = "_colvis",
        colvisTableAttrName = '_table';


    function parse(text){
        var json=[];
        $((text||"").split(",")).each(function(){
            json.push( this.replace(/(\S*):(\S*)/,'"$1":$2') );
        });
        return (new Function("","return "+('{'+json.join(",")+'}') ))();
    }



    function initTable(table){
        var $table = $(table).on("xhr.dt",resultConvert),columns =[],settings= parse ( $table.attr(tableAttrName) );

        $table.find("thead tr:eq(0) th").each(function(){
            var column = $.extend({}, {data:$(this).attr(columnDataName)},parse ( $(this).attr(columnAttrName) ) );
            columns.push( column );
        });

        $.extend( settings , {
            columns: columns,
            ajax:{
                url: $table.attr(tableUrlAttrName)
                //, dataType:"jsonp"
            }
        });
        console.log(settings)

        return $table.DataTable(settings);
    }


    function initColvis(colvis){

        var $colvis = $(colvis),table = $("#"+$colvis.attr(colvisTableAttrName)).DataTable();
        colvis = new $.fn.dataTable.ColVis( table, {
            buttonText: '显示/隐藏列',
            align: "right"||$covis.attr("align"),
            showAll: "显示所有列",
            restore: "恢复默认列"
        });
        $colvis.html( colvis.button() );
        return colvis;
    }


    $.fn.initDataTable = function (){

        $(this).find("."+tableClass).each(function(){

            new $.fn.dataTable.Api(this).destroy();
            initTable(this)
        });

        $(this).find("."+colvisClass).each(function(){
            initColvis(this);
        });
    }


    $(document).on("click",".table-refresh",function(){
        var table = $(this).attr("_table");
        if( table && $("#"+table).length > 0){
            new $.fn.dataTable.Api( "#"+table ).ajax.reload();
        }
    });









    function ajaxSubmit(){
        try {
            var $this=$(this);
            var $submitButton = $this.find(":submit,[form="+$this.attr("id")+"]");

            function beforeSend(){
                $submitButton.each(function(){
                    var $button=$(this);
                    $button.data("v",$button.prop("disabled"));
                    $button.prop("disabled","disabled");
                });
            }


            function complete(){
                $submitButton.each(function(){
                    var $button=$(this);
                    if( !$button.data("v") ){
                        $button.removeProp("disabled")
                    }else {
                        $button.prop("disabled",$button.data("v"));
                    }
                    $button.removeData("v");
                });
            }


            function success(r){

                if( !resultCheck(r) ){
                    return;
                }

                var parents = $this.parents(".modal");
                if( parents.length>0 ){
                    $(parents.get(0)).modal("hide");
                }
                try { $this.get(0).reset(); }catch (e){};

                var table = $this.attr("_table");
                if( table && $("#"+table).length > 0){
                    new $.fn.dataTable.Api( "#"+table ).ajax.reload();
                }
            }

            var defaultAjaxSetting = {
                type :"post", data: $this.serializeArray() , beforeSend:beforeSend ,complete : complete ,success : success
            };

            $.ajax($.extend({},defaultAjaxSetting,{url:$this.attr("action")}) );
        }catch (e){
            console.log(e)
        }finally {
            return false;
        }

    }

    $(document).on("submit",".ajaxForm",ajaxSubmit);

})();


/*** 简易模板插件 ***/
(function($){
    $.nano = function(template, data) {
        return template.replace(/\{([\w\.]*)\}/g, function (str, key) {
            var keys = key.split("."), value = data[keys.shift()];
            $.each(keys, function () { value = value[this]; });
            return (value === null || value === undefined) ? "" : value;
        });
    };
})(jQuery);


/** form 渲染插件 **/
(function($){

    $.fn.render=function(url){
        var $this=$(this);
        $.getJSON(url,function(r){
            var obj=$.resultConvert(r);
            $this.find("input,textarea,select").each(function(){
                $(this).val( obj[this.name]||"" );
            });
        })
    }
})(jQuery);