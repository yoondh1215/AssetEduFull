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

import kr.co.kfs.assetedu.model.Condition;
import kr.co.kfs.assetedu.model.Fnd01Fund;
import kr.co.kfs.assetedu.service.Com02CodeService;
import kr.co.kfs.assetedu.service.Fnd01FundService;
import lombok.extern.slf4j.Slf4j;

/**
 * 펀드 Controller
 * 
 */
@Controller
@Slf4j
@RequestMapping("/fund")
public class FundController {

	@Autowired
	private Fnd01FundService service;
	
	@Autowired
	private Com02CodeService codeService;

	@GetMapping("list")
	public String list(String searchText,  Model model) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("펀드리스트");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		model.addAttribute("pageTitle","펀드정보-리스트");
		Condition condition = new Condition();
		condition.put("searchText", searchText);
		List<Fnd01Fund> list = service.selectList(condition);
		model.addAttribute("list", list);
		return "/fund/list";
	}

	@GetMapping("insert")
	public String insert(Model model) {
		model.addAttribute("pageTitle","펀드등록");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("신규펀드 등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");

		model.addAttribute("fund",new Fnd01Fund());
		model.addAttribute("fundTypeList", codeService.codeList("FundType"));
		model.addAttribute("publicCdList", codeService.codeList("PublicCode"));
		model.addAttribute("unitCdList"  , codeService.codeList("FundUnitCode"));
		model.addAttribute("parentCdList", codeService.codeList("FundParentCode"));

		return "/fund/insert_form";
	}

	@PostMapping("insert")
	public String insert(@Valid @ModelAttribute("fund") Fnd01Fund fund, BindingResult bindingResult, RedirectAttributes redirectAttr, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("펀드정보를 테이블에 저장하고 리스트로 이동");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("fund: {}", fund);
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("fundTypeList", codeService.codeList("FundType"));
			model.addAttribute("publicCdList", codeService.codeList("PublicCode"));
			model.addAttribute("unitCdList"  , codeService.codeList("FundUnitCode"));
			model.addAttribute("parentCdList", codeService.codeList("FundParentCode"));
			return "/fund/insert_form";	
		}

		String msg;
		
		//펀드코드 중복체크
		Fnd01Fund checkFund = service.selectOne(fund);
		if(checkFund != null) {	
			msg = String.format("\"%s\" 펀드코드는 이미 \"%s\" 펀드로 등록되어있습니다.", checkFund.getFnd01FundCd(), checkFund.getFnd01FundNm());
			model.addAttribute("fundTypeList", codeService.codeList("FundType"));
			model.addAttribute("publicCdList", codeService.codeList("PublicCode"));
			model.addAttribute("unitCdList"  , codeService.codeList("FundUnitCode"));
			model.addAttribute("parentCdList", codeService.codeList("FundParentCode"));
			bindingResult.addError(new FieldError("", "", msg));
			return "/fund/insert_form";
		} else {
			int affectedCount = service.insert(fund);
			log.debug("DB에 적용된 갯수 : {}",affectedCount);

			msg = String.format("\"%s\" 펀드가 등록되었습니다", fund.getFnd01FundNm());
			redirectAttr.addAttribute("mode", "insert");
			redirectAttr.addAttribute("msg" , msg);
			return "redirect:/fund/success";
		}
	}

	@GetMapping("update")
	public String update(@ModelAttribute("fund") Fnd01Fund fund, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("펀드정보 상세보기 및 수정 form 표시");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		fund = service.selectOne(fund);

		model.addAttribute("fund", fund);
		model.addAttribute("fundTypeList", codeService.codeList("FundType"));
		model.addAttribute("publicCdList", codeService.codeList("PublicCode"));
		model.addAttribute("unitCdList"  , codeService.codeList("FundUnitCode"));
		model.addAttribute("parentCdList", codeService.codeList("FundParentCode"));

		return "/fund/update_form";
	}

	/**
	 * 펀드수정 데이터입력
	 * @param fund
	 * @param model
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@PostMapping("update")
	public String update_form(@ModelAttribute("fund") Fnd01Fund fund, Model model) throws UnsupportedEncodingException {
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("펀드정보 수정");
		log.debug("★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆★★★★★☆☆☆☆☆☆");
		log.debug("fund: {}", fund);

		service.update(fund);	
		String msg = String.format("\"%s\" 펀드가 수정되었습니다.", fund.getFnd01FundNm());
		return "redirect:/fund/success?mode=update&fundCd=" + fund.getFnd01FundCd()+"&msg=" + URLEncoder.encode(msg,"UTF-8");
	}

	@GetMapping("/delete")
	public String delete(@ModelAttribute Fnd01Fund fund) {
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("펀드정보삭제 삭제할 펀드코드 :" + fund.getFnd01FundCd());
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		
		int deletedCount = service.delete(fund);
		if(deletedCount > 0) {
			log.warn("펀드코드 : {}가 삭제되었습니다");
		}
		return "redirect:/fund/list";
	}

	@GetMapping("success")
	public String success(String msg, String mode, String fundCd, Model model) {
		model.addAttribute("pageTitle","펀드등록");
		model.addAttribute("msg", msg);
		model.addAttribute("mode", mode);
		model.addAttribute("fundCd", fundCd);
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		log.debug("펀드등록 화면");
		log.debug("★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★★");
		return "/fund/success";
	}
}
