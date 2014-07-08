package com.synature.pos;

import java.util.List;

public class MenuGroups {
	private List<MenuGroup> MenuGroup;
	private List<MenuDept> MenuDept;
	private List<MenuItem> MenuItem;	
	private List<MenuComment> MenuComment;
	private List<MenuFixComment> MenuFixComment;
	private List<MenuCommentGroup> MenuCommentGroup;
	
	public List<MenuCommentGroup> getMenuCommentGroup() {
		return MenuCommentGroup;
	}

	public void setMenuCommentGroup(List<MenuCommentGroup> menuCommentGroup) {
		MenuCommentGroup = menuCommentGroup;
	}

	public List<MenuFixComment> getMenuFixComment() {
		return MenuFixComment;
	}

	public void setMenuFixComment(List<MenuFixComment> menuFixComment) {
		MenuFixComment = menuFixComment;
	}

	public static class MenuCommentGroup{
		private int MenuCommentGroupID;
		private String MenuCommentGroupName_0;
		private String MenuCommentGroupName_1;
		private String UpdateDate;
		
		public int getMenuCommentGroupID() {
			return MenuCommentGroupID;
		}
		public void setMenuCommentGroupID(int menuCommentGroupID) {
			MenuCommentGroupID = menuCommentGroupID;
		}
		public String getMenuCommentGroupName_0() {
			return MenuCommentGroupName_0;
		}
		public void setMenuCommentGroupName_0(String menuCommentGroupName_0) {
			MenuCommentGroupName_0 = menuCommentGroupName_0;
		}
		public String getMenuCommentGroupName_1() {
			return MenuCommentGroupName_1;
		}
		public void setMenuCommentGroupName_1(String menuCommentGroupName_1) {
			MenuCommentGroupName_1 = menuCommentGroupName_1;
		}
		public String getUpdateDate() {
			return UpdateDate;
		}
		public void setUpdateDate(String updateDate) {
			UpdateDate = updateDate;
		}
		
	}
	
	public static class MenuFixComment {
		private int MenuItemID;
		private int MenuCommentID;
		
		public int getMenuItemID() {
			return MenuItemID;
		}
		public void setMenuItemID(int menuItemID) {
			MenuItemID = menuItemID;
		}
		public int getMenuCommentID() {
			return MenuCommentID;
		}
		public void setMenuCommentID(int menuCommentID) {
			MenuCommentID = menuCommentID;
		}
		
	}
	
	public static class MenuComment extends MenuItem{
		private int MenuCommentID;
		private String MenuCommentName_0;
		private String MenuCommentName_1;
		private String MenuCommentName_2;
		private String MenuCommentName_3;
		private int MenuCommentOrdering;
		private int MenuCommentGroupID;
		private float commentQty;
		private boolean isChecked;
		
		public int getMenuCommentGroupID() {
			return MenuCommentGroupID;
		}
		public void setMenuCommentGroupID(int menuCommentGroupID) {
			MenuCommentGroupID = menuCommentGroupID;
		}
		public boolean isChecked() {
			return isChecked;
		}
		public void setChecked(boolean isChecked) {
			this.isChecked = isChecked;
		}
		public void toggleCheck(){
			this.isChecked = !isChecked;
		}
		public float getCommentQty() {
			return commentQty;
		}
		public void setCommentQty(float commentQty) {
			this.commentQty = commentQty;
		}
		public int getMenuCommentID() {
			return MenuCommentID;
		}
		public void setMenuCommentID(int menuCommentID) {
			MenuCommentID = menuCommentID;
		}
		public String getMenuCommentName_0() {
			return MenuCommentName_0;
		}
		public void setMenuCommentName_0(String menuCommentName_0) {
			MenuCommentName_0 = menuCommentName_0;
		}
		public String getMenuCommentName_1() {
			return MenuCommentName_1;
		}
		public void setMenuCommentName_1(String menuCommentName_1) {
			MenuCommentName_1 = menuCommentName_1;
		}
		public String getMenuCommentName_2() {
			return MenuCommentName_2;
		}
		public void setMenuCommentName_2(String menuCommentName_2) {
			MenuCommentName_2 = menuCommentName_2;
		}
		public String getMenuCommentName_3() {
			return MenuCommentName_3;
		}
		public void setMenuCommentName_3(String menuCommentName_3) {
			MenuCommentName_3 = menuCommentName_3;
		}
		public int getMenuCommentOrdering() {
			return MenuCommentOrdering;
		}
		public void setMenuCommentOrdering(int menuCommentOrdering) {
			MenuCommentOrdering = menuCommentOrdering;
		}
	}
	
	public static class MenuItem extends ProductGroups.Products {
		private int MenuItemID;
		private int MenuDeptID;
		private int MenuGroupID;
		private String MenuName_0;
		private String MenuName_1;
		private String MenuName_2;
		private String MenuName_3;
		private String MenuName_4;
		private String MenuName_5;
		private String MenuDesc_0;
		private String MenuDesc_1;
		private String MenuDesc_2;
		private String MenuDesc_3;
		private String MenuDesc_4;
		private String MenuDesc_5;
		private String MenuLongDesc_0;
		private String MenuLongDesc_1;
		private String MenuLongDesc_2;
		private String MenuLongDesc_3;
		private String MenuLongDesc_4;
		private String MenuLongDesc_5;
		private String MenuShortName_0;
		private String MenuShortName_1;
		private String MenuShortName_2;
		private String MenuShortName_3;
		private String MenuShortName_4;
		private String MenuShortName_5;
		private int MenuItemOrdering;
		private String MenuImageLink;
		private int MenuActivate;
		
		public int getMenuActivate() {
			return MenuActivate;
		}
		public void setMenuActivate(int menuActivate) {
			MenuActivate = menuActivate;
		}
		public int getMenuItemID() {
			return MenuItemID;
		}
		public void setMenuItemID(int menuItemID) {
			MenuItemID = menuItemID;
		}
		public int getMenuDeptID() {
			return MenuDeptID;
		}
		public void setMenuDeptID(int menuDeptID) {
			MenuDeptID = menuDeptID;
		}
		public int getMenuGroupID() {
			return MenuGroupID;
		}
		public void setMenuGroupID(int menuGroupID) {
			MenuGroupID = menuGroupID;
		}
		public String getMenuName_0() {
			return MenuName_0;
		}
		public void setMenuName_0(String menuName_0) {
			MenuName_0 = menuName_0;
		}
		public String getMenuName_1() {
			return MenuName_1;
		}
		public void setMenuName_1(String menuName_1) {
			MenuName_1 = menuName_1;
		}
		public String getMenuName_2() {
			return MenuName_2;
		}
		public void setMenuName_2(String menuName_2) {
			MenuName_2 = menuName_2;
		}
		public String getMenuName_3() {
			return MenuName_3;
		}
		public void setMenuName_3(String menuName_3) {
			MenuName_3 = menuName_3;
		}
		public String getMenuName_4() {
			return MenuName_4;
		}
		public void setMenuName_4(String menuName_4) {
			MenuName_4 = menuName_4;
		}
		public String getMenuName_5() {
			return MenuName_5;
		}
		public void setMenuName_5(String menuName_5) {
			MenuName_5 = menuName_5;
		}
		public String getMenuDesc_0() {
			return MenuDesc_0;
		}
		public void setMenuDesc_0(String menuDesc_0) {
			MenuDesc_0 = menuDesc_0;
		}
		public String getMenuDesc_1() {
			return MenuDesc_1;
		}
		public void setMenuDesc_1(String menuDesc_1) {
			MenuDesc_1 = menuDesc_1;
		}
		public String getMenuDesc_2() {
			return MenuDesc_2;
		}
		public void setMenuDesc_2(String menuDesc_2) {
			MenuDesc_2 = menuDesc_2;
		}
		public String getMenuDesc_3() {
			return MenuDesc_3;
		}
		public void setMenuDesc_3(String menuDesc_3) {
			MenuDesc_3 = menuDesc_3;
		}
		public String getMenuDesc_4() {
			return MenuDesc_4;
		}
		public void setMenuDesc_4(String menuDesc_4) {
			MenuDesc_4 = menuDesc_4;
		}
		public String getMenuDesc_5() {
			return MenuDesc_5;
		}
		public void setMenuDesc_5(String menuDesc_5) {
			MenuDesc_5 = menuDesc_5;
		}
		public String getMenuLongDesc_0() {
			return MenuLongDesc_0;
		}
		public void setMenuLongDesc_0(String menuLongDesc_0) {
			MenuLongDesc_0 = menuLongDesc_0;
		}
		public String getMenuLongDesc_1() {
			return MenuLongDesc_1;
		}
		public void setMenuLongDesc_1(String menuLongDesc_1) {
			MenuLongDesc_1 = menuLongDesc_1;
		}
		public String getMenuLongDesc_2() {
			return MenuLongDesc_2;
		}
		public void setMenuLongDesc_2(String menuLongDesc_2) {
			MenuLongDesc_2 = menuLongDesc_2;
		}
		public String getMenuLongDesc_3() {
			return MenuLongDesc_3;
		}
		public void setMenuLongDesc_3(String menuLongDesc_3) {
			MenuLongDesc_3 = menuLongDesc_3;
		}
		public String getMenuLongDesc_4() {
			return MenuLongDesc_4;
		}
		public void setMenuLongDesc_4(String menuLongDesc_4) {
			MenuLongDesc_4 = menuLongDesc_4;
		}
		public String getMenuLongDesc_5() {
			return MenuLongDesc_5;
		}
		public void setMenuLongDesc_5(String menuLongDesc_5) {
			MenuLongDesc_5 = menuLongDesc_5;
		}
		public int getMenuItemOrdering() {
			return MenuItemOrdering;
		}
		public void setMenuItemOrdering(int menuItemOrdering) {
			MenuItemOrdering = menuItemOrdering;
		}
		public String getMenuImageLink() {
			return MenuImageLink;
		}
		public void setMenuImageLink(String menuImageLink) {
			MenuImageLink = menuImageLink;
		}
		public String getMenuShortName_0() {
			return MenuShortName_0;
		}
		public void setMenuShortName_0(String menuShortName_0) {
			MenuShortName_0 = menuShortName_0;
		}
		public String getMenuShortName_1() {
			return MenuShortName_1;
		}
		public void setMenuShortName_1(String menuShortName_1) {
			MenuShortName_1 = menuShortName_1;
		}
		public String getMenuShortName_2() {
			return MenuShortName_2;
		}
		public void setMenuShortName_2(String menuShortName_2) {
			MenuShortName_2 = menuShortName_2;
		}
		public String getMenuShortName_3() {
			return MenuShortName_3;
		}
		public void setMenuShortName_3(String menuShortName_3) {
			MenuShortName_3 = menuShortName_3;
		}
		public String getMenuShortName_4() {
			return MenuShortName_4;
		}
		public void setMenuShortName_4(String menuShortName_4) {
			MenuShortName_4 = menuShortName_4;
		}
		public String getMenuShortName_5() {
			return MenuShortName_5;
		}
		public void setMenuShortName_5(String menuShortName_5) {
			MenuShortName_5 = menuShortName_5;
		}
		@Override
		public String toString() {
			return MenuName_0;
		}
	}
	
	public static class MenuDept{
		private int MenuDeptID;
		private int MenuGroupID;
		private String MenuDeptName_0;
		private String MenuDeptName_1;
		private String MenuDeptName_2;
		private String MenuDeptName_3;
		private String MenuDeptName_4;
		private String MenuDeptName_5;
		private int MenuDeptOrdering;
		private String UpdateDate;
		private int Activate;
		
		public int getActivate() {
			return Activate;
		}
		public void setActivate(int activate) {
			Activate = activate;
		}
		public int getMenuDeptID() {
			return MenuDeptID;
		}
		public void setMenuDeptID(int menuDeptID) {
			MenuDeptID = menuDeptID;
		}
		public int getMenuGroupID() {
			return MenuGroupID;
		}
		public void setMenuGroupID(int menuGroupID) {
			MenuGroupID = menuGroupID;
		}
		public String getMenuDeptName_0() {
			return MenuDeptName_0;
		}
		public void setMenuDeptName_0(String menuDeptName_0) {
			MenuDeptName_0 = menuDeptName_0;
		}
		public String getMenuDeptName_1() {
			return MenuDeptName_1;
		}
		public void setMenuDeptName_1(String menuDeptName_1) {
			MenuDeptName_1 = menuDeptName_1;
		}
		public String getMenuDeptName_2() {
			return MenuDeptName_2;
		}
		public void setMenuDeptName_2(String menuDeptName_2) {
			MenuDeptName_2 = menuDeptName_2;
		}
		public String getMenuDeptName_3() {
			return MenuDeptName_3;
		}
		public void setMenuDeptName_3(String menuDeptName_3) {
			MenuDeptName_3 = menuDeptName_3;
		}
		public String getMenuDeptName_4() {
			return MenuDeptName_4;
		}
		public void setMenuDeptName_4(String menuDeptName_4) {
			MenuDeptName_4 = menuDeptName_4;
		}
		public String getMenuDeptName_5() {
			return MenuDeptName_5;
		}
		public void setMenuDeptName_5(String menuDeptName_5) {
			MenuDeptName_5 = menuDeptName_5;
		}
		public int getMenuDeptOrdering() {
			return MenuDeptOrdering;
		}
		public void setMenuDeptOrdering(int menuDeptOrdering) {
			MenuDeptOrdering = menuDeptOrdering;
		}
		public String getUpdateDate() {
			return UpdateDate;
		}
		public void setUpdateDate(String updateDate) {
			UpdateDate = updateDate;
		}
		
		@Override
		public String toString() {
			return MenuDeptName_0;
		}
	}
	
	public static class MenuGroup{
		private int MenuGroupID;
		private int MenuDeptID;
		private String MenuGroupName_0;
		private String MenuGroupName_1;
		private String MenuGroupName_2;
		private String MenuGroupName_3;
		private String MenuGroupName_4;
		private String MenuGroupName_5;
		private int MenuGroupType;
		private int MenuGroupOrdering;
		private String UpdateDate;
		private int Activate;
		
		public int getActivate() {
			return Activate;
		}
		public void setActivate(int activate) {
			Activate = activate;
		}
		public int getMenuGroupID() {
			return MenuGroupID;
		}
		public void setMenuGroupID(int menuGroupID) {
			MenuGroupID = menuGroupID;
		}
		public String getMenuGroupName_0() {
			return MenuGroupName_0;
		}
		public void setMenuGroupName_0(String menuGroupName_0) {
			MenuGroupName_0 = menuGroupName_0;
		}
		public String getMenuGroupName_1() {
			return MenuGroupName_1;
		}
		public void setMenuGroupName_1(String menuGroupName_1) {
			MenuGroupName_1 = menuGroupName_1;
		}
		public String getMenuGroupName_2() {
			return MenuGroupName_2;
		}
		public void setMenuGroupName_2(String menuGroupName_2) {
			MenuGroupName_2 = menuGroupName_2;
		}
		public String getMenuGroupName_3() {
			return MenuGroupName_3;
		}
		public void setMenuGroupName_3(String menuGroupName_3) {
			MenuGroupName_3 = menuGroupName_3;
		}
		public String getMenuGroupName_4() {
			return MenuGroupName_4;
		}
		public void setMenuGroupName_4(String menuGroupName_4) {
			MenuGroupName_4 = menuGroupName_4;
		}
		public String getMenuGroupName_5() {
			return MenuGroupName_5;
		}
		public void setMenuGroupName_5(String menuGroupName_5) {
			MenuGroupName_5 = menuGroupName_5;
		}
		public int getMenuGroupType() {
			return MenuGroupType;
		}
		public void setMenuGroupType(int menuGroupType) {
			MenuGroupType = menuGroupType;
		}
		public int getMenuGroupOrdering() {
			return MenuGroupOrdering;
		}
		public void setMenuGroupOrdering(int menuGroupOrdering) {
			MenuGroupOrdering = menuGroupOrdering;
		}
		public String getUpdateDate() {
			return UpdateDate;
		}
		public void setUpdateDate(String updateDate) {
			UpdateDate = updateDate;
		}
		public int getMenuDeptID() {
			return MenuDeptID;
		}
		public void setMenuDeptID(int menuDeptID) {
			MenuDeptID = menuDeptID;
		}
	}

	public List<MenuGroup> getMenuGroup() {
		return MenuGroup;
	}

	public void setMenuGroup(List<MenuGroup> menuGroup) {
		MenuGroup = menuGroup;
	}

	public List<MenuDept> getMenuDept() {
		return MenuDept;
	}

	public void setMenuDept(List<MenuDept> menuDept) {
		MenuDept = menuDept;
	}

	public List<MenuItem> getMenuItem() {
		return MenuItem;
	}

	public void setMenuItem(List<MenuItem> menuItem) {
		MenuItem = menuItem;
	}

	public List<MenuComment> getMenuComment() {
		return MenuComment;
	}

	public void setMenuComment(List<MenuComment> menuComment) {
		MenuComment = menuComment;
	}
}
