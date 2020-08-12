package com.aurora.community.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import com.aurora.community.service.AlphaService;
import com.fasterxml.jackson.core.sym.Name;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/alpha")
public class AlphaController {
    @Autowired
    private AlphaService alphaService;

    @RequestMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "hello string boot";
    }
    
    @RequestMapping("/data")
    @ResponseBody
    public String getData() {
        return alphaService.find();
    }

    @RequestMapping("/http")
    public void http(HttpServletRequest request, HttpServletResponse response) {
        // request data
        System.out.println(request.getMethod());
        System.out.println(request.getServletPath());
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            System.out.println(name + ": " + value);
        }
        System.out.println(request.getParameter("code"));

        // responce data
        response.setContentType("text/html;charset=utf-8");
        try (
            PrintWriter writer = response.getWriter();
        ){
            writer.write("<h1>牛客网</h1>");
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    // handle GET (default, get data from web browser)
    //  /students?current=1&limit=20
    @RequestMapping(path="/students", method=RequestMethod.GET)
    @ResponseBody
    public String getStudents(
        @RequestParam(name="current",required = false, defaultValue = "1")int current, 
        @RequestParam(name = "limit", required = false, defaultValue = "20") int limit) { 
        System.out.println(current);
        System.out.println(limit);
        return "Some students";
    }
    // /students/123  id in the path
    @RequestMapping(path="/student/{id}", method=RequestMethod.GET)
    @ResponseBody
    public String getStudent(@PathVariable("id") int id){
        System.out.println(id);
        return "a student";
    }

    // handle POST 
    @RequestMapping(path = "/student", method = RequestMethod.POST)
    // @ResponseBody
    public ModelAndView saveStudent(String name, int age){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", name);
        modelAndView.addObject("age", age);
        modelAndView.setViewName("/demo/view");
        System.out.println(name);
        System.out.println(age);
        return modelAndView; 
    }

    // handle html data
    @RequestMapping(path = "/teacher", method = RequestMethod.GET)
    public ModelAndView getTeacher() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("name", "hebe");
        modelAndView.addObject("age", 30);
        modelAndView.setViewName("/demo/view");
        return modelAndView;
    }
    @RequestMapping(path = "/school/{id}?", method = RequestMethod.GET)
    public String getSchool(Model model, @PathVariable("id") int id, 
        @RequestParam(name = "name", defaultValue = "Peking University") String name) {
        model.addAttribute("name", name);
        model.addAttribute("age", 80);
        model.addAttribute("id", id);
        return "demo/view";
    }

    // handle json data asynchronized  -- e.g.the name has been used
    // auto translate to json
    @RequestMapping(path = "/employee", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getEmp() {
        Map<String, Object> emp = new HashMap<>();
        emp.put("age", 23);
        emp.put("name", "Anna");
        emp.put("salary", 10000);
        return emp;
    }
    @RequestMapping(path = "/employees", method = RequestMethod.GET)
    @ResponseBody
    public List<Map<String, Object>> getEmps() {
        List<Map<String, Object>> list = new ArrayList<>();
        Map<String, Object> emp = new HashMap<>();
        emp.put("age", 23);
        emp.put("name", "Anna");
        emp.put("salary", 10000);
        list.add(emp);
        Map<String, Object> emp2 = new HashMap<>();
        emp2.put("age", 33);
        emp2.put("name", "Lison");
        emp2.put("salary", 12000);
        list.add(emp2);
        Map<String, Object> emp3 = new HashMap<>();
        emp3.put("age", 29);
        emp3.put("name", "Dio");
        emp3.put("salary", 19000);
        list.add(emp3);
        return list;
    }
}