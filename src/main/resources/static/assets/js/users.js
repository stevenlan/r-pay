var c_f = [
    { name: "modifiedTime", title: "Application time", type: "text", width: 150 },
    { name: "email", title: "Email", type: "text", width: 100 },
    { name: "from", title: "From", type: "text", width: 100 },
    { name: "kycType", type: "text", title: "Type", width: 50 },
    { name: "nickName", type: "text", title : "Name", width: 100 }] ,
 a_f = [
    { name: "email", title: "Email", type: "text", width: 150 },
    { name: "from", title: "From", type: "text", width: 100 },
    { name: "nickName", type: "text", title : "Name", width: 100 },
    { name: "kycType", type: "text", title: "Type", width: 50 },
    { name: "modifiedTime", title: "Approved Time", type: "text", width: 150 }] ,
 r_f =[
    { name: "email", title: "Email", type: "text", width: 100 },
    { name: "from", title: "From", type: "text", width: 100 },
    { name: "nickName", type: "text", title : "Name", width: 100 },
    { name: "kycType", type: "text", title: "Type", width: 50 },
    { name: "modifiedTime", title: "Rejected Time", type: "text", width: 150 },
    { name: "reason", type: "text", title: "Reason", width: 100 }] ;
 s_f = [{ name: "modifiedTime", title: "Approved Time", type: "text", width: 150 },
           { name: "email", title: "Email", type: "text", width: 100 },
           { name: "from", title: "From", type: "text", width: 100 },
           { name: "nickName", title: "Name", type: "text", title : "name", width: 100 },
           { name: "kycType", title: "Type", type: "text", title: "type", width: 50 },
           { type: "control", title: "check", itemTemplate: function(value, item) {
             //var $result = $([]);
             var input = $("<input>").addClass("jsgrid-button").addClass("jsgrid-search-button")
             .attr({
               type: "button",
               title: "Add Order"
             })
             .on("click", function(e) {
               console.log(item);
               $("#orderInfo")[0].style.display = "";
               $("#kycType").text(item.kycType) ;
               $("#tradeName").text(item.nickName) ;
               $("#tradeEmail").text(item.email) ;
               $("form input[name='userId']").val(item.id);
             });
             return input ;
           }}] ;