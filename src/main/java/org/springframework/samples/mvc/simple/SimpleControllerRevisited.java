package org.springframework.samples.mvc.simple;

import org.springframework.samples.mvc.form.FormBean;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SimpleControllerRevisited {

	@RequestMapping(value="/simple/revisited", method=RequestMethod.GET)
    public @ResponseBody
    MultiValueMap<String, String> writeForm() {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("foo", "bar");
        map.add("fruit", null);
        return map;
    }
}
