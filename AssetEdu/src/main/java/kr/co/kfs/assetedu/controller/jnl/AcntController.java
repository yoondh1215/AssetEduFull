package kr.co.kfs.assetedu.controller.jnl;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Jnl10Acnt;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Jnl10AcntService;
import lombok.extern.slf4j.Slf4j;

/**
 * 계정과목 Controller
 *
 */
@Slf4j
@Controller
@RequestMapping("/jnl/acnt")
public class AcntController {
	
	private final String baseUrl = "/jnl/acnt";
	
	@Autowired
	private Jnl10AcntService service;
	
	@Autowired
	private Com02CodeService codeService;
	
	
	@GetMapping("list")
	public String list(String searchText,   Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("계정과목");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","계정과목");
		
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Jnl10Acnt> list = service.selectList(condition);
		model.addAttribute("list", list);
		return baseUrl + "/list";
	}
	
	@GetMapping("insert")
	public String insert(Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("계정과목 추가 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","계정과목추가");
		model.addAttribute("jnl10Acnt"     ,new Jnl10Acnt());
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
		return baseUrl + "/insert_form";
	}
	
	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("jnl10Acnt") Jnl10Acnt acnt, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("계정과목 저장 후 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("acnt: {}", acnt);

		String msg;
		
		//계정코드 중복체크
		Jnl10Acnt checkAcnt = service.selectOne(acnt);
		if(checkAcnt != null) {
			msg = String.format("\"%s\" 계정코드는 이미 \"%s\"으로 등록되어 있습니다", acnt.getJnl10AcntCd(), checkAcnt.getJnl10AcntNm());
			model.addAttribute("jnl10Acnt",acnt);
			model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
			model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));
			bindingResult.addError(new FieldError("", "", msg));
			return baseUrl + "/insert_form";
		} else {
			int affectedCount = 0;
			affectedCount = service.insert(acnt);
			log.debug("DB에 적용된 갯수 : {}",affectedCount );

			msg = String.format("\"%s\" 계정과목이(가) 등록되었습니다", acnt.getJnl10AcntNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg", msg);
			return "redirect:" + baseUrl + "/success";
		}
	}
	
	@GetMapping("/update/{jnl10AcntCd}")
	public String update(@PathVariable("jnl10AcntCd") String jnl10AcntCd, Model model)  {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("계정과목 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		Jnl10Acnt acnt = service.selectOne(jnl10AcntCd);
		model.addAttribute("jnl10Acnt", acnt);
		model.addAttribute("acntAttrCodeList", codeService.codeList("AcntAttrCode"));
		model.addAttribute("drcrTypeList", codeService.codeList("DrcrType"));		
		return baseUrl + "/update_form";
	}
//
	@PostMapping("update")
	public String update_form(@ModelAttribute("jnl10Acnt") Jnl10Acnt jnl10Acnt, RedirectAttributes redirectAttr) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("계정과목 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("acnt: {}", jnl10Acnt);
		service.update(jnl10Acnt);	
		
		//성공화면을 redirect
		String msg = String.format("\"%s\" 계정과목이(가) 수정되었습니다.", jnl10Acnt.getJnl10AcntNm());
		redirectAttr.addAttribute("mode", "update");
		log.debug("acnt:{}",jnl10Acnt);
		redirectAttr.addAttribute("jnl10AcntCd", jnl10Acnt.getJnl10AcntCd());
		redirectAttr.addAttribute("msg", msg);
		return "redirect:" + baseUrl +"/success"; 
	}
//	
	@GetMapping("/delete/{jnl10AcntCd}")
	public String delete(@PathVariable("jnl10AcntCd") String jnl10AcntCd) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("acnt삭제할 id : {}",jnl10AcntCd);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(jnl10AcntCd);
		if(deletedCount > 0) {
			
			log.warn("acnt id : {}가 삭제되었습니다");
		}
		return "redirect:" + baseUrl + "/list";
	}
//
	@GetMapping("success")
	public String success(String msg, String mode, String jnl10AcntCd, Model model) {
		model.addAttribute("pageTitle","계정과목 관리");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("jnl10AcntCd", jnl10AcntCd);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("계정과목 추가 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		return baseUrl +"/success";
	}
}
