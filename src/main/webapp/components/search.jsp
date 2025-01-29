<%--
  Created by IntelliJ IDEA.
  User: danielecalisti
  Date: 29/01/25
  Time: 10:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<label class="relative block">
    <span class="sr-only">Search</span>
    <span class="absolute inset-y-0 left-0 flex items-center pl-2">
        <svg class="h-5 w-5 fill-slate-300" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
            <path fill-rule="evenodd" d="M12.9 14.32a8 8 0 1 1 1.42-1.42l4.36 4.36a1 1 0 0 1-1.42 1.42l-4.36-4.36zM14 8a6 6 0 1 0-12 0 6 6 0 0 0 12 0z" clip-rule="evenodd" />
        </svg>
    </span>
    <select id="search" class="w-full border border-slate-300 rounded-md py-2 pl-3 pr-3 shadow-sm sm:text-sm">
    </select>
</label>
