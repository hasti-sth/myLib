package org.example.controller;

import org.example.model.Member;
import org.example.model.enums.MemberLevel;
import org.example.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }



    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(
            @PathVariable int id) {

        Member member =
                memberService.getMemberById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(member);
    }


    @PostMapping
    public ResponseEntity<Member> createMember(
            @RequestBody MemberRequest request) {

        Member member =
                memberService.addMember(
                        request.name(),
                        request.password(),
                        request.level()
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(member);
    }



    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(
            @PathVariable int id,
            @RequestBody Member member) {

        member.setId(id);

        boolean updated =
                memberService.updateMember(member);

        if (!updated) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(
                memberService.getMemberById(id)
        );
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(
            @PathVariable int id) {

        boolean deleted =
                memberService.deleteMember(id);

        if (!deleted) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/renew")
    public ResponseEntity<Member> renewMembership(
            @PathVariable int id) {

        Member member =
                memberService.getMemberById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        memberService.renewMember(member);

        return ResponseEntity.ok(member);
    }



    @GetMapping("/{id}/membership")
    public ResponseEntity<Integer> getMembership(
            @PathVariable int id) {

        Member member =
                memberService.getMemberById(id);

        if (member == null) {
            return ResponseEntity.notFound().build();
        }

        int remainDays =
                memberService.getRemainDays(member);

        return ResponseEntity.ok(remainDays);
    }
    public record MemberRequest(
            String name,
            String password,
            MemberLevel level
    ) {
    }
}
