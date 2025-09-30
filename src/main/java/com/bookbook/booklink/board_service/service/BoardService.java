package com.bookbook.booklink.board_service.service;

import com.bookbook.booklink.auth_service.model.Member;
import com.bookbook.booklink.board_service.model.Board;
import com.bookbook.booklink.board_service.model.BoardCategory;
import com.bookbook.booklink.board_service.model.dto.request.BoardCreateDto;
import com.bookbook.booklink.board_service.model.dto.request.BoardUpdateDto;
import com.bookbook.booklink.board_service.model.dto.response.BoardDetailDto;
import com.bookbook.booklink.board_service.model.dto.response.BoardListDto;
import com.bookbook.booklink.board_service.repository.BoardRepository;
import com.bookbook.booklink.common.event.LockEvent;
import com.bookbook.booklink.common.exception.CustomException;
import com.bookbook.booklink.common.exception.ErrorCode;
import com.bookbook.booklink.common.service.IdempotencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final IdempotencyService idempotencyService;

    @Transactional
    public UUID createBoard(BoardCreateDto boardCreateDto, Member member, String traceId) {
        String key = idempotencyService.generateIdempotencyKey("board:create", traceId);

        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());

        Board newBoard = Board.toEntity(boardCreateDto, member);

        Board savedBoard = boardRepository.save(newBoard);

        return savedBoard.getId();

    }

    @Transactional
    public UUID updateBoard(BoardUpdateDto boardUpdateDto, Member member, String traceId) {
        String key = idempotencyService.generateIdempotencyKey("board:update", traceId);

        idempotencyService.checkIdempotency(key, 1,
                () -> LockEvent.builder().key(key).build());
        
        Board existingBoard = getBoardById(boardUpdateDto.getBoardId());

        existingBoard.update(boardUpdateDto);

        Board savedBoard = boardRepository.save(existingBoard);

        return savedBoard.getId();

    }

    @Transactional
    public void deleteBoard(UUID boardId) {
        Board board = getBoardById(boardId);

        board.delete();

        boardRepository.save(board);
    }

    @Transactional(readOnly = true)
    public List<BoardListDto> getBoards(String title, BoardCategory category) {
        List<Board> boardList = boardRepository.findByTitleAndCategory(title, category);

        return boardList.stream().map(BoardListDto::fromEntity).toList();
    }

    @Transactional
    public BoardDetailDto getBoard(UUID boardId) {

        Board board = getBoardById(boardId);

        board.view();

        boardRepository.save(board);

        return BoardDetailDto.fromEntity(board);

    }

    @Transactional
    public void likeBoard(UUID boardId) {
        Board board = getBoardById(boardId);
        board.like();
        boardRepository.save(board);
    }

    public Board getBoardById(UUID boardId) {
        return boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
    }
}