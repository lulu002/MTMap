(function($){
    var bathUrl = window.location.protocol + "//" + window.location.host +"/maitian/v1";
    Grain = {
        isQuerying : false,
        grainTable : $('#grainList'), 
        pagination : $('.pagination'), 
        pager : {"totalRows":0,"pageSize":10,"currentPage":1,"totalPages":0,"startRow":0},   
        pageClick : function(num){
            var t = this, pager = t.pager;
            if(num == '-1'){ //prev
                t.query(pager.currentPage-1)
            }else if(num == '-2'){ //next
                t.query(pager.currentPage+1);
            }else{
                t.query(num);
            }
        },       
        renderTable : function(data){
            if(data instanceof Array != true || !data.length) return;
            var t = this, html = '';
            for(var i = 0; i < data.length; i++){
                var item = data[i];
                html += '<tr>'
                     +    '<td>'+item[1]+'</td>'
                     +    '<td>'+item[2]+":"+item[14]+'</td>'
                     +    '<td>'+item[22]+'</td>'
                     +    '<td>'+item[17]+'</td>'
                     +    '<td>'+item[5]+'</td>'
                     +    '<td><a class="grain-edit"><i class="fa fa-fw fa-edit"></i></a></td>'
                     +  '</tr>';
            }
            t.grainTable.find('tbody').html(html);
        },
        renderPagination : function(){
            var t = this, pagination = t.pagination, pager = t.pager, html='';
            var disabled = pager.currentPage == 1 ? 'disabled' : '',
                onclick = pager.currentPage == 1 ? '' : 'onclick="Grain.pageClick(-1)"';

            html += '<li class="'+disabled+'"'+ onclick +'><a aria-label="Previous"><span aria-hidden="true">«</span></a></li>';
            for (var i = 1; i <= pager.totalPages ; i++) {
                var active = i == pager.currentPage ? 'active' : '';
                html += '<li class="'+active+'" onclick="Grain.pageClick('+i+')"><a>'+i+'</a></li>';
            };

            disabled = pager.currentPage == pager.totalPages ? 'disabled' : '';
            onclick = pager.currentPage == pager.totalPages ? '' : 'onclick="Grain.pageClick(-2)"';
            html += '<li class="'+disabled+'"'+onclick+'><a aria-label="Next"><span aria-hidden="true">»</span></a></li>';
            pagination.html(html);
        },
        query : function(currentPage){
            var t = this;
            if(t.isQuerying) return;
            t.isQuerying = true;
            var promise = $.ajax({
                url : bathUrl + "/grain/query_grain_list.json",
                type : "POST",
                dataType : "json",
                contentType : "application/json",
                data : JSON.stringify({"currentPage":currentPage})
            });

            

            promise.done(function(data){
                t.isQuerying = false;
                if(data.success == 'true'){ 
                    var data = data.data;
                    t.renderTable(data.grain);
                    t.pager = data.pager;
                    t.renderPagination();
                }else{
                    console.log("error_code:"+data.error_code+","+ "error_message:"+data.error_message);
                }
            });

            promise.fail(function(data){
                t.isQuerying = false;
                console.log(data);
            });
        },
        queryByIpt : function(){
            var t = this, pager = t.pager;
            var pageNum = $('#pageNum').val();
                pageNum = pageNum.replace(/\s/g,'');
            if(!/^[1-9]\d*$|^0$/.test(pageNum)){ //限制了0132这种形式
                alert('请输入数字');
                return;
            }

            if(pageNum > pager.totalPages || pageNum <= 0){
                alert('请正确输入范围');
                return;
            }

            t.query(parseInt(pageNum));
        },
        init : function(){
            var t = this;
            t.query(1);
        }
    };

    Grain.init();
})(jQuery);
