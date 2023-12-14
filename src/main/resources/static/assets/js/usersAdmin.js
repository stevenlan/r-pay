var c_f = [
            { name: "modifiedTime", title: "Application time", type: "text", width: 150 },
            { name: "email", title: "Email", type: "text", width: 100 },
            { name: "from", title: "From", type: "text", width: 100 },
            { name: "kycType", type: "text", title: "Type", width: 50 },
            { name: "nickName", type: "text", title : "Name", width: 100 },
            {type: "control", itemTemplate: function(value, item) {
              //var $result = $([]);
              var input = $("<input>").addClass("jsgrid-button").addClass("jsgrid-edit-button")
              .attr({
                type: "button",
                title: "approve"
              })
              .on("click", function(e) {
                //approve
                location.replace('/user/approve?userId='+item.id);
              });
              return input ;
             }}],
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