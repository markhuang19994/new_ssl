package common

import example.ssl.SslApplication
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

/**
 * @author MarkHuang*
 * <ul>
 *  <li>3/9/20, MarkHuang,new
 * </ul>
 * @since 3/9/20
 */
@Controller
@RequestMapping('/info')
class Project {

    @Autowired
    private ApplicationContext ctx

    @ResponseBody
    @RequestMapping('/apis')
    String test1() {
        return SslApplication.getHandlersPrintString(ctx).replaceAll('\r?\n', '<br/>')
    }

}
