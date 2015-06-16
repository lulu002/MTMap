(function($){
    var baseUrl = window.location.protocol + "//" + window.location.host +"/backyard/v1";
    Grain = {
        isQuerying : false,
        grainTable : $('#grainList'), 
        grains : [],
        pagination : $('.pagination'), 
        pager : {"totalRows":0,"pageSize":10,"currentPage":1,"totalPages":0,"startRow":0},
        showGrainDetail : function(grainId){
        	var t = this, grain = t.queryLocalGrainById(grainId);
        	if(null == grain) throw ('grain is null');
        	var message = $('#grainDetail').html();
        		message = message.replace(/\{gid\}/g,grain.gid)
        						 .replace(/\{mcateId\}/,grain.mcateId)
        						 .replace(/\{siteName\}/,grain.siteId)
        						 .replace(/\{userId\}/,grain.userId)
        						 .replace(/\{recommend\}/,grain.recommend)
        						 .replace(/\{text\}/,grain.text)
        						 .replace(/\{isPublic\}/,grain.isPublic);
        	BootstrapDialog.show({
                title: '麦粒详情',
                message: message,
                cssClass: 'login-dialog',
                buttons: [{
                    label: '取消',
                    cssClass: 'btn-default',
                    action: function(dialog){
                        dialog.close();
                    }
                },{
                    label: '保存',
                    cssClass: 'btn-primary',
                    action: function(dialog){
                        t.updateGrain(grain.gid);
                    }
                }]
            });
        },
        queryLocalGrainById : function(grainId){
        	var t = this, grains = t.grains;
        	for(var i = 0 ; i < grains.length; i++){
        		var item = grains[i];
        		if(item.gid == grainId)return item;
        	}
        	return null;
        },
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
                     +    '<td>'+item.mcateId+'</td>'
                     +    '<td>'+item.siteId+'</td>'
                     +    '<td>'+item.userId+'</td>'
                     +    '<td>'+item.isPublic+'</td>'
                     +    '<td>'+item.text+'</td>'
                     +    '<td><a class="grain-edit" onclick="Grain.showGrainDetail(\''+item.gid+'\');"><i class="fa fa-fw fa-edit"></i></a></td>'
                     +  '</tr>';
            }
            t.grainTable.find('tbody').html(html);
        },
        renderPagination : function(){
            var t = this, pagination = t.pagination, pager = t.pager, html='';
            var disabled = pager.currentPage == 1 ? 'disabled' : '',
                onclick = pager.currentPage == 1 ? '' : 'onclick="Grain.pageClick(-1)"';

            html += '<li class="'+disabled+'"'+ onclick +'><a aria-label="Previous"><span aria-hidden="true">«</span></a></li>';
            var start, end;
            if(pager.totalPages <= 9 ){
            	start = 1;
            	end = pager.totalPages;
            }else{
            	var tmp = pager.currentPage -4;
            	start =  tmp > 1 ? tmp : 1;
            	tmp = pager.currentPage + 4;
            	end = tmp > pager.totalPages ? pager.totalPages : tmp;
            }
            var max = pager.totalPages > 9 ? pager.currentPage + 4 : page.totalPages;
            for (var i = start; i <= end ; i++) {
                var active = i == pager.currentPage ? 'active' : '';
                html += '<li class="'+active+'" onclick="Grain.pageClick('+i+')"><a>'+i+'</a></li>';
            };

            disabled = pager.currentPage == pager.totalPages ? 'disabled' : '';
            onclick = pager.currentPage == pager.totalPages ? '' : 'onclick="Grain.pageClick(-2)"';
            html += '<li class="'+disabled+'"'+onclick+'><a aria-label="Next"><span aria-hidden="true">»</span></a></li>';
            
            html+='<li><a>共 ' + pager.totalPages + '页</a></li>';
            pagination.html(html);
        },
        query : function(currentPage){
            var t = this;
            if(t.isQuerying) return;
            t.isQuerying = true;
            var promise = $.ajax({
                url : baseUrl + "/grain/query_grain_list.json",
                type : "POST",
                dataType : "json",
                contentType : "application/json",
                data : JSON.stringify({"currentPage":currentPage})
            });

            promise.done(function(data){
                t.isQuerying = false;
                if(data.success == 'true'){ 
                    var data = data.data;
                    t.grains = data.grain;
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
        addImageTag : function(grainId){
            var form = $('#'+grainId);
            form.append('<label>选择图片:</label><input type="file" name="images">');
        },
        updateGrain : function(grainId){
            var formData = new FormData($("#"+grainId)[0]);
            $.ajax({
                url : baseUrl + '/grain/update.json',
                type : 'POST',
                data : formData,
                processData : false,
                contentType : false
            }).done(function(re){
                if(!re.success) console.log(re.error_message);
                
            });
        },
        init : function(){
            var t = this;
            t.query(1);
        }
    };

    Grain.init();
})(jQuery);
