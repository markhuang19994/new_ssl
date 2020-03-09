package common

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody

@Controller
@RequestMapping('/play')
class Play {

    def random = new Random()
    def answer = 78

    @ResponseBody
    @RequestMapping("/guessNumber/{number}")
    String saveCustomer(@PathVariable Integer number) {
        def msg = ''

        if (number == answer) {
            answer = random.nextInt(10**2)
            msg = 'Congratulations guess number is correct'
        } else if (number <= answer) {
            msg = 'Correct number is more then ' + String.valueOf(number)
        } else {
            msg = 'Correct number is less then ' + String.valueOf(number)
        }
        return msg
    }

}

