package kr.co.kfs.assetedu.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.co.kfs.assetedu.model.Com01Corp;
import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.service.Com01CorpService;
import kr.co.kfs.assetedu.service.Com02CodeService;
import lombok.extern.slf4j.Slf4j;

/**
 * 기관정보 Controller
 * 
 */
@Controller
@Slf4j
@RequestMapping("/corp")
public class CorpController {

	@Autowired
	private Com01CorpService service;
	
	@Autowired
	private Com02CodeService codeService;

	@GetMapping("list")
	public String list(String searchText,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("기관정보 리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","기관정보-리스트");
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Com01Corp> list = service.selectList(condition);
		model.addAttribute("list", list);
		return "/corp/list";
	}

	@GetMapping("insert")
	public String insert(Model model) {
		model.addAttribute("pageTitle","기관등록");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("신규기관 등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		model.addAttribute("corp",new Com01Corp());
		model.addAttribute("corpTypeList", codeService.codeList("CorpType"));

		return "/corp/insert_form";
	}

	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("corp") Com01Corp corp, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("기관정보를 테이블에 저장하고 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("corp: {}", corp);

		if(bindingResult.hasErrors()) {
			model.addAttribute("corpTypeList", codeService.codeList("CorpType"));
			return "/corp/insert_form";	
		}

		String msg;
		
		//기관코드 중복체크
		Com01Corp checkCorp = service.selectOne(corp);
		if(checkCorp != null) {
			msg = String.format("\"%s\" 코드는 이미 \"%s\" 으로 등록되어있습니다.", checkCorp.getCom01CorpCd(), checkCorp.getCom01CorpNm());
			bindingResult.addError(new FieldError("", "", msg));
			model.addAttribute("corpTypeList", codeService.codeList("CorpType"));
			return "/corp/insert_form";	
		} else {
			int affectedCount = service.insert(corp);
			log.debug("DB에 적용된 갯수 : {}",affectedCount);

			msg = String.format("\"%s\" 기관정보가 등록되었습니다", corp.getCom01CorpNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/corp/success";
		}
	}

	@GetMapping("update")
	public String update(@ModelAttribute("corp") Com01Corp corp, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("기관정보 상세보기 및 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		corp = service.selectOne(corp);

		model.addAttribute("corp", corp);
		model.addAttribute("corpTypeList", codeService.codeList("CorpType"));

		return "/corp/update_form";
	}
	/**
	 * 기관정보수정 데이터입력
	 * @param corp
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping("update")
	public String update_form(@ModelAttribute("corp") Com01Corp corp, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("기관정보 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("corp: {}", corp);

		service.update(corp);	
		String msg = String.format("\"%s\" 기관정보가 수정되었습니다.", corp.getCom01CorpNm());
		return "redirect:/corp/success?mode=update&corpCd=" + corp.getCom01CorpCd()+"&msg=" + URLEncoder.encode(msg,"UTF-8");
	}

	@GetMapping("/delete")
	public String delete(@ModelAttribute Com01Corp corp) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("기관정보삭제 삭제할 기관코드 :" + corp.getCom01CorpCd());
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(corp);
		if(deletedCount > 0) {
			log.warn("기관코드 : {}가 삭제되었습니다");
		}
		return "redirect:/corp/list";
	}

	@GetMapping("success")
	public String success(String msg, String mode, String corpCd, Model model) {
		model.addAttribute("pageTitle","기관정보등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("corpCd", corpCd);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("기관정보등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		return "/corp/success";
	}
}
