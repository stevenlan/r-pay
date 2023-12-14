package com.rpay.controller;


/**
 * 处理用户查询审批设定等级结算等功能
 * @author steven
 */
//@Slf4j
//@Controller
//@RequestMapping("user")
//@RequiredArgsConstructor
public class UserController extends BaseController {
    //private final UserService userService;
    //private final KycDetailService kycService ;


    /*
     * 获取当前登录的用户信息
     * @return

    @PostMapping("/get")
    @ResponseBody
    public R get() {
        User user = getLoginUser() ;
        return R.succeed(user) ;
    }*/

    /*
    @GetMapping({"","index"})
    public String user(Model model) {
        User user = getLoginUser() ;
        model.addAttribute("user",user) ;
        return "user" ;
    }*/

    /*
    @GetMapping("trades")
    public String trades(Model model) {
        User user = getLoginUser() ;
        model.addAttribute("user",user) ;
        return "userTrades" ;
    }*/

    /*
     * 查询用户
     * @param query 查询参数封装
     * @return 返回json
    @PostMapping("approveUsers")
    @ResponseBody
    public R findApproveUsers(UserQuery<User> query) {
        User user = getLoginUser() ;
        LambdaQueryChainWrapper<User> userWra = userService.lambdaQuery() ;
        //long diff = System.currentTimeMillis() - 1000*60*60*24 ;
        //userWra.ge(User::getCreateTime,new Date(diff)) ;
        userWra.eq(!StringUtils.equals(user.getProviderId(),user.getInviteCode())
                ,User::getInviteCode,user.getProviderId()) ;
        userWra.eq(User::getParentId,-1) ;
        userWra.eq(User::getUserStatus,query.getOptions()) ;
        if ( StringUtils.isNotEmpty(query.getSearch()) ) {
            String search = query.getSearch() ;
            userWra.and(i -> i.like(User::getNickName,search).or()
                    .like(User::getEmail,search)) ;
        }
        userWra.orderByDesc(User::getModifiedTime) ;
        query = userWra.page(query) ;

        //处理关联reason和关联的推广人
        if ( null != query.getRecords() && !query.getRecords().isEmpty() ) {
            final List<User> result = query.getRecords();
            List<String> formId = new ArrayList<>() ;
            List<String> pid = new ArrayList<>();
            result.forEach(u -> {
                pid.add(u.getProviderId());
                formId.add(u.getInviteCode()) ;
            });
            List<User> forms = userService.list(new LambdaQueryWrapper<User>().in(User::getProviderId,formId)) ;
            result.forEach( r-> {
                User match = forms.stream()
                        .filter((u) -> StringUtils.equals(u.getProviderId(),r.getInviteCode()))
                        .findFirst().get() ;
                r.setFrom(match.getNickName()) ;
            });
            if (query.getOptions() == 2) {
                kycService.lambdaQuery()
                        .in(KycDetail::getProviderId, pid).list()
                        .forEach(kyc -> {
                            User match = result.stream()
                                    .filter((u) -> StringUtils.equals(u.getProviderId(),kyc.getProviderId()))
                                    .findFirst().get() ;
                            match.setReason(kyc.getReason()) ;
//                            result.forEach(u -> {
//                                if (StringUtils.equals(u.getProviderId(), kyc.getProviderId())) {
//                                    u.setReason(kyc.getReason());
//                                }
//                            });
                        });
            }
        }

        query.getRecords().forEach(u -> {
            u.setPassword("");
        });
        return R.succeed(query, "查询成功");
    }*/

    /*@GetMapping("approve")
    public String approve(Integer userId, Model model) {
        User user = userService.getById(userId) ;
        KycDetail kyc = kycService.getOne(new LambdaQueryWrapper<KycDetail>().eq(KycDetail::getProviderId,user.getProviderId())) ;

        model.addAttribute("targetUser",user) ;
        String page  ;
        if ( "individual".equals(user.getKycType()) ) {
            IndVO info = JSON.parseObject(kyc.getKycInfo(), IndVO.class) ;
            IndVO bank = JSON.parseObject(kyc.getKycBank(), IndVO.class) ;
            IndVO att = JSON.parseObject(kyc.getKycAttachment(), IndVO.class) ;
            model.addAttribute("info",info) ;
            model.addAttribute("bank",bank) ;
            model.addAttribute("att",att) ;
            page = "appInd" ;
        } else {
            InsVO info = JSON.parseObject(kyc.getKycInfo(), InsVO.class) ;
            InsVO bank = JSON.parseObject(kyc.getKycBank(), InsVO.class) ;
            InsVO att = JSON.parseObject(kyc.getKycAttachment(), InsVO.class) ;
            model.addAttribute("info",info) ;
            model.addAttribute("bank",bank) ;
            model.addAttribute("att",att) ;
            page = "appIns" ;
        }
        model.addAttribute("user",getLoginUser()) ;
        return page ;
    }*/

    /*
    @PostMapping("approve")
    @ResponseBody
    public R approve(Approve approve) {
        if (StringUtils.equals("approved",approve.getOptions())) {
            if ( StringUtils.isBlank(approve.getEsp()) || !NumberUtil.isDouble(approve.getEsp())) {
                return R.failed("Esp未按格式要求填写，审批失败") ;
            }
            if ( StringUtils.isBlank(approve.getMinValue()) || !NumberUtil.isNumber(approve.getMinValue())) {
                return R.failed("minValue未按格式要求填写，审批失败") ;
            }
            return kycService.approve(approve) ;
        } else {
            return kycService.reject(approve) ;
        }
    }*/
}
