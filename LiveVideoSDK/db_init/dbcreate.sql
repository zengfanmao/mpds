USE [dbCCS_TEST]
GO
/****** Object:  UserDefinedFunction [dbo].[f_split]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE function [dbo].[f_split](@c varchar(2000),@split varchar(2)) 
returns @t table(col varchar(20)) 
as 
    begin 
      while(charindex(@split,@c)<>0) 
        begin 
          insert @t(col) values (substring(@c,1,charindex(@split,@c)-1)) 
          set @c = stuff(@c,1,charindex(@split,@c),'') 
        end 
      insert @t(col) values (@c) 
      return 
    end 


GO
/****** Object:  Table [dbo].[tb_sim_gps]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tb_sim_gps](
	[gpsLongitude] [decimal](10, 7) NOT NULL,
	[gpsLatitude] [decimal](10, 7) NOT NULL,
	[gpsTime] [datetime] NULL
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbApiTokens]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbApiTokens](
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[uCode] [varchar](36) NULL,
	[apiToken] [varchar](36) NULL,
	[createdTime] [datetime] NULL,
	[isDel] [bit] NULL,
	[recSN] [timestamp] NOT NULL,
 CONSTRAINT [PK_tbApiTokens] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbAppLogs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbAppLogs](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[fCode] [varchar](30) NOT NULL,
	[appVersionCode] [varchar](30) NOT NULL,
	[appApiUrl] [varchar](350) NULL,
	[appType] [varchar](60) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbAppVersions]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbAppVersions](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[appType] [varchar](30) NOT NULL,
	[appVersionNo] [int] NOT NULL,
	[appVersionName] [varchar](80) NOT NULL,
	[appPublishTime] [datetime] NOT NULL,
	[appFeatures] [text] NOT NULL,
	[appTitle] [varchar](200) NOT NULL,
	[appDownloadUrl] [varchar](360) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbBroadcastRoom]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbBroadcastRoom](
	[ID] [uniqueidentifier] NOT NULL,
	[rCode] [nvarchar](50) NOT NULL,
	[uCode] [nvarchar](50) NOT NULL,
	[createdTime] [datetime] NULL,
 CONSTRAINT [PK_tbBroadcastRoom_1] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCameras]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCameras](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[camCode] [char](18) NOT NULL,
	[camName] [nchar](16) NULL,
	[camType] [nchar](6) NULL,
	[camAddr] [nvarchar](128) NULL,
	[camLongitude] [decimal](10, 7) NOT NULL,
	[camLatitude] [decimal](10, 7) NOT NULL,
	[camHeight] [tinyint] NOT NULL,
	[camView] [nchar](5) NULL,
	[camAngle] [tinyint] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCameras] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCameras] UNIQUE NONCLUSTERED 
(
	[camCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseArticles]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseArticles](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[arCode] [char](24) NOT NULL,
	[arType] [nchar](10) NULL,
	[arName] [nvarchar](30) NULL,
	[arBrand] [nchar](20) NULL,
	[arProdArea] [nvarchar](40) NULL,
	[arModel] [nchar](10) NULL,
	[arSpec] [nchar](10) NULL,
	[arPat] [nchar](20) NULL,
	[arMaterial] [nchar](20) NULL,
	[arColor] [nchar](10) NULL,
	[arQuality] [nchar](20) NULL,
	[arCount] [bigint] NOT NULL,
	[arCountUnit] [nchar](5) NULL,
	[arWeight] [decimal](11, 2) NOT NULL,
	[arWeightUnit] [nchar](5) NULL,
	[arVal] [bigint] NOT NULL,
	[arDesc] [nvarchar](70) NULL,
	[arPhoto] [char](12) NULL,
	[arRemark] [ntext] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseArticles] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseArticles] UNIQUE NONCLUSTERED 
(
	[arCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseClues]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseClues](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[ccType] [nchar](10) NULL,
	[ccAbstract] [nvarchar](128) NULL,
	[ccFile] [text] NULL,
	[uCode] [char](12) NOT NULL,
	[ccCreateTime] [datetime] NULL,
	[uCodeChk] [char](12) NULL,
	[ccCheckTime] [datetime] NULL,
	[ccCheckResult] [nchar](6) NOT NULL,
	[ccSourceID] [char](18) NULL,
	[ccSourceType] [nchar](8) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseClues] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseCollection]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseCollection](
	[ID] [uniqueidentifier] NOT NULL,
	[caseCode] [varchar](36) NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[messageId] [varchar](36) NOT NULL,
	[remark] [varchar](200) NULL,
	[uCode] [varchar](36) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseCompanys]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseCompanys](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[coCode] [char](24) NOT NULL,
	[coClass] [nchar](5) NOT NULL,
	[coName] [nvarchar](40) NULL,
	[coType] [nchar](8) NULL,
	[coCorporateRep] [nvarchar](30) NULL,
	[coTel] [char](20) NULL,
	[coEmail] [nvarchar](40) NULL,
	[coArea] [nchar](8) NULL,
	[coAddress] [nvarchar](40) NULL,
	[coRemark] [ntext] NULL,
	[coPhoto] [char](12) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseCompanys] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseCompanys] UNIQUE NONCLUSTERED 
(
	[coCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseDeployDevs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseDeployDevs](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[devCode] [char](12) NOT NULL,
	[uCode] [char](12) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseDeployDevs] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseDeployDevs] UNIQUE NONCLUSTERED 
(
	[csCode] ASC,
	[devCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseDeploys]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseDeploys](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[uCode] [char](24) NOT NULL,
	[rName] [nchar](10) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseDeploys] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseDeploys] UNIQUE NONCLUSTERED 
(
	[csCode] ASC,
	[uCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseGoings]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseGoings](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](36) NOT NULL,
	[cgType] [varchar](20) NULL,
	[cgAbstract] [nvarchar](64) NULL,
	[uCode] [varchar](36) NOT NULL,
	[cgCreateTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[PositionRemark] [varchar](200) NULL,
	[PositionLongitude] [decimal](12, 7) NULL,
	[PositionLatitude] [decimal](12, 7) NULL,
	[PositionName] [varchar](100) NULL,
	[uUnitCode] [varchar](50) NULL,
 CONSTRAINT [PK_tbCaseGoings] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseGps]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseGps](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[gpsTargetType] [nchar](10) NOT NULL,
	[devCode] [varchar](36) NULL,
	[uCode] [char](12) NOT NULL,
	[gpsLongitude] [decimal](10, 7) NOT NULL,
	[gpsLatitude] [decimal](10, 7) NOT NULL,
	[gpsTime] [datetime] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[altitude] [decimal](10, 5) NULL,
	[speed] [decimal](10, 5) NULL,
	[accelerationX] [decimal](10, 5) NULL,
	[accelerationY] [decimal](10, 5) NULL,
	[accelerationZ] [decimal](10, 5) NULL,
 CONSTRAINT [PK_tbCaseGps] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseGps] UNIQUE NONCLUSTERED 
(
	[csCode] ASC,
	[gpsTargetType] ASC,
	[devCode] ASC,
	[uCode] ASC,
	[gpsTime] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseGuns]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseGuns](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[gunCode] [char](24) NOT NULL,
	[gunModel] [nchar](10) NULL,
	[gunNum] [char](15) NULL,
	[gunPointSize] [smallint] NOT NULL,
	[gunFeature] [nvarchar](70) NULL,
	[gunIssueDate] [date] NULL,
	[gunLicenseNum] [char](20) NULL,
	[gunIsReg] [tinyint] NOT NULL,
	[gunOwnerWorkUnit] [nvarchar](40) NULL,
	[gunOwner] [nvarchar](30) NULL,
	[gunBulletCount] [smallint] NOT NULL,
	[gunPhoto] [char](12) NULL,
	[gunRemark] [ntext] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseGuns] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseGuns] UNIQUE NONCLUSTERED 
(
	[gunCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseLivingShows]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseLivingShows](
	[ID] [uniqueidentifier] NOT NULL,
	[caseCode] [varchar](36) NOT NULL,
	[rtmpUrl] [varchar](200) NOT NULL,
	[userCode] [varchar](36) NOT NULL,
	[livingState] [bit] NOT NULL,
	[startTime] [datetime] NOT NULL,
	[endTime] [datetime] NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[sourceType] [varchar](20) NULL,
	[deviceCode] [varchar](20) NULL,
	[Cumulative] [int] NULL,
	[fFirstFrame] [varchar](36) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseMessages]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseMessages](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[msgType] [nchar](10) NULL,
	[msgAbstract] [nvarchar](max) NULL,
	[msgFile] [char](14) NULL,
	[uCode] [char](12) NOT NULL,
	[msgTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[uLatitude] [decimal](10, 7) NULL,
	[uLongitude] [decimal](10, 7) NULL,
	[uPositionName] [varchar](80) NULL,
	[msgFromType] [varchar](50) NOT NULL,
	[msgToCode] [varchar](50) NULL,
 CONSTRAINT [PK_tbCaseMessages] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseMission]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseMission](
	[ID] [uniqueidentifier] NOT NULL,
	[caseCode] [varchar](36) NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[missionStatus] [int] NULL,
	[missionLimiTime] [int] NULL,
	[toPositionName] [varchar](100) NULL,
	[toPositionLatitude] [decimal](10, 7) NULL,
	[toPositionLongitude] [decimal](10, 7) NULL,
	[missionRemark] [varchar](500) NULL,
	[routeDistance] [decimal](10, 2) NULL,
	[createdUserCode] [varchar](36) NULL,
	[createdPositionName] [varchar](100) NULL,
	[createdPositionLatitude] [decimal](10, 7) NULL,
	[createdPositionLongitude] [decimal](10, 7) NULL,
	[missionType] [varchar](50) NULL,
	[finishTime] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseMissionDistribution]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseMissionDistribution](
	[ID] [uniqueidentifier] NOT NULL,
	[caseCode] [varchar](36) NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[missionId] [varchar](36) NOT NULL,
	[missionPersonStatus] [int] NULL,
	[persionFinishTime] [datetime] NULL,
	[finishPositionName] [varchar](100) NULL,
	[finishPositionLatitude] [decimal](10, 7) NULL,
	[finishPositionLongitude] [decimal](10, 7) NULL,
	[userCode] [varchar](50) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCases]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCases](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[csName] [nvarchar](120) NULL,
	[csType] [nvarchar](90) NULL,
	[csAddress] [nchar](40) NULL,
	[csTime] [datetime] NULL,
	[csTime_h] [datetime] NULL,
	[csStatus] [nchar](8) NULL,
	[csDesc] [nvarchar](1000) NULL,
	[csEndFlag] [nchar](3) NULL,
	[csEndTime] [datetime] NULL,
	[csAccept] [nchar](16) NULL,
	[csContact] [nvarchar](30) NULL,
	[csTel] [nvarchar](20) NULL,
	[csPlan] [ntext] NULL,
	[uCode] [char](12) NOT NULL,
	[csCreateTime] [datetime] NULL,
	[csRptName] [nvarchar](30) NULL,
	[csRptSex] [tinyint] NOT NULL,
	[csRptAge] [tinyint] NOT NULL,
	[csRptWorkUnit] [nvarchar](40) NULL,
	[csRptLiveAddr] [nvarchar](40) NULL,
	[csRptTel] [char](20) NULL,
	[csRptTime] [datetime] NULL,
	[csRecept] [nvarchar](30) NULL,
	[csRptType] [nchar](5) NULL,
	[csReceptTime] [datetime] NULL,
	[csHowDiscover] [nchar](5) NULL,
	[csDiscoverTime] [datetime] NULL,
	[csArea] [nchar](8) NULL,
	[csSceneType] [nchar](3) NULL,
	[csHurtLevel] [nchar](5) NULL,
	[csRegDate] [date] NULL,
	[csRegInst] [nchar](16) NULL,
	[csFlag] [nchar](5) NULL,
	[csEmail] [nvarchar](40) NULL,
	[csReregReason] [nchar](4) NULL,
	[csSuspCount] [tinyint] NOT NULL,
	[csHurtCount] [tinyint] NOT NULL,
	[csDeadCount] [tinyint] NOT NULL,
	[csLoseVal] [bigint] NOT NULL,
	[csChoseOpp] [nvarchar](40) NULL,
	[csChoseLoc] [nvarchar](36) NULL,
	[csChoseObj] [nvarchar](32) NULL,
	[csChoseArt] [nvarchar](68) NULL,
	[csCrimeTrick] [nvarchar](72) NULL,
	[csCrimePat] [nvarchar](56) NULL,
	[csCrimeTool] [nvarchar](68) NULL,
	[csEndDept] [nchar](16) NULL,
	[csEndWay] [nvarchar](21) NULL,
	[csEndType] [nchar](4) NULL,
	[csCaptureVal] [bigint] NOT NULL,
	[csCaptCount] [smallint] NOT NULL,
	[csGrpCount] [smallint] NOT NULL,
	[csGrpKind] [nchar](3) NULL,
	[csGrpInvCount] [smallint] NOT NULL,
	[csEndDesc] [ntext] NULL,
	[csRescueType] [nchar](5) NULL,
	[csRescueCount] [smallint] NOT NULL,
	[csEndChainCount] [smallint] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[csLatitude] [decimal](10, 7) NULL,
	[csLongitude] [decimal](10, 7) NULL,
	[csUpdateTime] [datetime] NULL,
 CONSTRAINT [PK_tbCases] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCases] UNIQUE NONCLUSTERED 
(
	[csCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseSecurities]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseSecurities](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[secCode] [char](24) NOT NULL,
	[secType] [nchar](10) NULL,
	[secFaceVal] [bigint] NOT NULL,
	[secCount] [smallint] NOT NULL,
	[secVal] [bigint] NOT NULL,
	[secIssue] [nvarchar](40) NULL,
	[secIssueDate] [date] NULL,
	[secDesc] [nvarchar](70) NULL,
	[secNum] [varchar](40) NULL,
	[secPhoto] [char](12) NULL,
	[secRemark] [ntext] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseSecurities] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseSecurities] UNIQUE NONCLUSTERED 
(
	[secCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseSrcFiles]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseSrcFiles](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[fCode] [char](14) NOT NULL,
	[sfAbstract] [nvarchar](128) NULL,
	[sfType] [nchar](10) NULL,
	[sfSrcID] [char](18) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseSrcFiles] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseSrcFiles] UNIQUE NONCLUSTERED 
(
	[fCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseSuspects]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseSuspects](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[pCode] [char](24) NOT NULL,
	[pClass] [nchar](9) NULL,
	[pName] [nvarchar](30) NULL,
	[pByname] [nvarchar](93) NULL,
	[pSex] [tinyint] NOT NULL,
	[spAge] [tinyint] NOT NULL,
	[spAge_h] [tinyint] NOT NULL,
	[pCountry] [nvarchar](13) NULL,
	[pNation] [nchar](6) NULL,
	[spHeight] [smallint] NOT NULL,
	[spHeight_h] [smallint] NOT NULL,
	[pIdNum] [char](18) NULL,
	[pCareer] [nvarchar](20) NULL,
	[spAccent] [nvarchar](18) NULL,
	[spFaceShape] [nvarchar](12) NULL,
	[pBodyShape] [nchar](5) NULL,
	[spOthFeature] [nvarchar](40) NULL,
	[pSpclFeature] [nvarchar](41) NULL,
	[pSurfaceMark] [nvarchar](40) NULL,
	[spGoodAt] [nvarchar](24) NULL,
	[pIdentify] [nvarchar](24) NULL,
	[pBloodType] [nchar](3) NULL,
	[spCrimes] [nchar](3) NULL,
	[pLiveArea] [nchar](8) NULL,
	[pAddress] [nvarchar](40) NULL,
	[pBirthArea] [nchar](8) NULL,
	[pBirthAddr] [nvarchar](40) NULL,
	[spFprCode] [char](24) NULL,
	[spFeature] [nvarchar](70) NULL,
	[pDress] [nvarchar](70) NULL,
	[pPhoto] [char](12) NULL,
	[arrMcCause] [nvarchar](24) NULL,
	[arrDoDept] [nchar](16) NULL,
	[arrDoDate] [date] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseSuspects] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseSuspects] UNIQUE NONCLUSTERED 
(
	[pCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseTrailEvs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseTrailEvs](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[evCode] [char](24) NOT NULL,
	[trName] [nvarchar](66) NULL,
	[trDesc] [nvarchar](88) NULL,
	[evName] [nvarchar](75) NULL,
	[evDesc] [nvarchar](350) NULL,
	[evMicro] [nvarchar](55) NULL,
	[scnPhoto] [char](12) NULL,
	[scnDesc] [nvarchar](1000) NULL,
	[evRemark] [ntext] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseTrailEvs] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseTrailEvs] UNIQUE NONCLUSTERED 
(
	[evCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseVehicles]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseVehicles](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[vCode] [char](24) NOT NULL,
	[vClass] [nchar](7) NULL,
	[vType] [nchar](9) NULL,
	[vCarNum] [nchar](20) NULL,
	[vBrand] [char](32) NULL,
	[vModel] [char](32) NULL,
	[vColor] [nchar](10) NULL,
	[vIsFraudNum] [bit] NOT NULL,
	[vFeature] [nvarchar](70) NULL,
	[vOwner] [nvarchar](30) NULL,
	[vOwnerAddr] [nvarchar](40) NULL,
	[vOwnerTel] [char](16) NULL,
	[vOwnerEmail] [varchar](40) NULL,
	[vPhoto] [char](12) NULL,
	[vRemark] [ntext] NULL,
	[vCarNumType] [nchar](10) NULL,
	[vEngineNum] [char](30) NULL,
	[vVin] [char](25) NULL,
	[vInsStatus] [nvarchar](70) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseVehicles] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseVehicles] UNIQUE NONCLUSTERED 
(
	[vCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCaseVictims]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCaseVictims](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NULL,
	[vtCode] [char](24) NOT NULL,
	[vtClass] [nchar](9) NULL,
	[pName] [nvarchar](30) NULL,
	[pSex] [tinyint] NOT NULL,
	[pCountry] [nvarchar](13) NULL,
	[pNation] [nchar](6) NULL,
	[pBodyShape] [nchar](5) NULL,
	[pSpclFeature] [nvarchar](41) NULL,
	[pSurfaceMark] [nvarchar](40) NULL,
	[pIdentify] [nvarchar](24) NULL,
	[pBloodType] [nchar](3) NULL,
	[pAge] [tinyint] NOT NULL,
	[spAge_h] [tinyint] NOT NULL,
	[pCareer] [nvarchar](20) NULL,
	[pLiveArea] [nchar](8) NULL,
	[pAddress] [nvarchar](40) NULL,
	[pBirthArea] [nchar](8) NULL,
	[pBirthAddr] [nvarchar](40) NULL,
	[pDress] [nvarchar](70) NULL,
	[pPhoto] [char](12) NULL,
	[pBirth] [date] NULL,
	[vtAssaultMode] [nvarchar](18) NULL,
	[vtInjury] [nchar](4) NULL,
	[pWorkUnit] [nvarchar](40) NULL,
	[pEmail] [nvarchar](40) NULL,
	[ubDeadTime] [datetime] NULL,
	[ubDeadTime_h] [datetime] NULL,
	[ubBodyLen] [smallint] NOT NULL,
	[ubFootLen] [smallint] NOT NULL,
	[ubTooth] [nvarchar](70) NULL,
	[ubBodyIntegrity] [nchar](3) NULL,
	[ubBodyFresh] [nchar](4) NULL,
	[ubTakeWith] [nvarchar](70) NULL,
	[pRemark] [ntext] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCaseVictims] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCaseVictims] UNIQUE NONCLUSTERED 
(
	[vtCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbCrimCerts]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbCrimCerts](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[pCode] [char](24) NOT NULL,
	[pCertType] [nchar](10) NOT NULL,
	[pCertNum] [char](20) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbCrimCerts] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbCrimCerts] UNIQUE NONCLUSTERED 
(
	[pCode] ASC,
	[pCertType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDepartments]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDepartments](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[dCode] [nvarchar](50) NOT NULL,
	[dName] [nchar](16) NULL,
	[dFather] [nvarchar](50) NULL,
	[dDesc] [nvarchar](64) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbDepartments] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbDepartments] UNIQUE NONCLUSTERED 
(
	[dCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDeviceIMDatas]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDeviceIMDatas](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[LastModifyTime] [datetime] NULL,
	[devSn] [varchar](16) NOT NULL,
	[csCode] [varchar](24) NOT NULL,
	[latitude] [decimal](10, 7) NOT NULL,
	[longitude] [decimal](10, 7) NOT NULL,
	[positionName] [varchar](260) NOT NULL,
	[altitude] [decimal](10, 5) NULL,
	[speed] [decimal](10, 5) NULL,
	[accelerationX] [decimal](10, 5) NULL,
	[accelerationY] [decimal](10, 5) NULL,
	[accelerationZ] [decimal](10, 5) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDevices]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDevices](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[devCode] [char](12) NOT NULL,
	[devName] [nchar](16) NULL,
	[devType] [nchar](10) NULL,
	[devBrand] [nchar](10) NULL,
	[devModel] [nchar](16) NULL,
	[devPDate] [date] NULL,
	[devGPeriod] [tinyint] NOT NULL,
	[devSTime] [date] NULL,
	[uCode] [char](12) NOT NULL,
	[devStatus] [nchar](8) NULL,
	[devPhoto] [char](14) NULL,
	[devRemark] [nvarchar](64) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[devSN] [varchar](50) NULL,
 CONSTRAINT [PK_tbDevices] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbDevices] UNIQUE NONCLUSTERED 
(
	[devCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDictFields]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDictFields](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[tField] [char](20) NOT NULL,
	[dicField] [char](20) NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbDictFields] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbDictFields] UNIQUE NONCLUSTERED 
(
	[tField] ASC,
	[dicField] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDictItems]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDictItems](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[tName] [char](20) NOT NULL,
	[tField] [char](20) NOT NULL,
	[dicCode] [char](16) NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbDictItems] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbDictItems] UNIQUE NONCLUSTERED 
(
	[csCode] ASC,
	[tName] ASC,
	[tField] ASC,
	[dicCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDicts]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDicts](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[dicField] [char](20) NOT NULL,
	[dicValue] [nvarchar](255) NOT NULL,
	[dicValDesc] [nvarchar](255) NULL,
	[dicCode] [char](16) NOT NULL,
	[dicFather] [char](16) NULL,
	[dicLevel] [tinyint] NOT NULL,
	[dicValAbbr] [char](16) NULL,
	[uCode] [char](12) NULL,
	[dicEditTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbDicts] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbDicts] UNIQUE NONCLUSTERED 
(
	[dicField] ASC,
	[dicFather] ASC,
	[dicCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDiscussionGroupMenbers]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDiscussionGroupMenbers](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[discussionCode] [varchar](16) NOT NULL,
	[createdUserCode] [varchar](50) NOT NULL,
	[createdUserName] [varchar](50) NULL,
	[uCode] [varchar](24) NOT NULL,
	[csCode] [varchar](24) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbDiscussionGroups]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbDiscussionGroups](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[createdTime] [datetime] NULL,
	[discussionCode] [varchar](16) NOT NULL,
	[discussionName] [varchar](16) NOT NULL,
	[createdUserCode] [varchar](50) NOT NULL,
	[createdUserName] [varchar](50) NULL,
	[csCode] [varchar](24) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbEndChainCases]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbEndChainCases](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[csCode] [char](24) NOT NULL,
	[csEndChainCode] [char](24) NOT NULL,
	[csEndChainArea] [nchar](6) NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbEndChainCases] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbEndChainCases] UNIQUE NONCLUSTERED 
(
	[csCode] ASC,
	[csEndChainCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbFileDetails]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbFileDetails](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[attRsCode] [char](12) NULL,
	[fCode] [char](14) NOT NULL,
	[fName] [char](20) NOT NULL,
	[fSize] [bigint] NOT NULL,
	[fAbstract] [nvarchar](128) NULL,
	[fFirstFrame] [char](14) NULL,
	[fStartTime] [datetime] NULL,
	[fEndTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[fRelativePath] [varchar](300) NULL,
	[virtualId] [varchar](36) NULL,
 CONSTRAINT [PK_tbFileDetails] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbFileDetails] UNIQUE NONCLUSTERED 
(
	[fCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbFunctions]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbFunctions](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[funName] [nchar](16) NULL,
	[funDesc] [nvarchar](40) NULL,
	[funController] [varchar](50) NULL,
	[funAction] [varchar](50) NULL,
	[mdID] [uniqueidentifier] NULL,
	[funOrder] [int] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[funType] [int] NOT NULL,
	[PID] [uniqueidentifier] NULL,
 CONSTRAINT [PK_tbFunctions] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbFunctions_GE]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbFunctions_GE](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[funName] [nchar](16) NULL,
	[funDesc] [nvarchar](40) NULL,
	[funController] [varchar](50) NULL,
	[funAction] [varchar](50) NULL,
	[mdID] [uniqueidentifier] NULL,
	[funOrder] [int] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[funType] [int] NOT NULL,
	[PID] [uniqueidentifier] NULL,
 CONSTRAINT [PK_tbFunctions_GE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbLingYangDevices]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbLingYangDevices](
	[device] [varchar](36) NOT NULL,
	[state] [int] NULL,
	[tracker_ip] [varchar](20) NULL,
	[tracker_port] [int] NULL,
	[public_ip] [varchar](20) NULL,
	[public_port] [int] NULL,
	[local_ip] [varchar](20) NULL,
	[local_port] [int] NULL,
	[config_type] [varchar](10) NULL,
	[conn_key] [int] NULL,
	[relay_ip] [varchar](20) NULL,
	[relay_port] [int] NULL,
	[caretedTime] [datetime] NULL,
	[modifyTime] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[device] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbLogonLogs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbLogonLogs](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[loSN] [char](12) NOT NULL,
	[uCode] [char](12) NOT NULL,
	[onTime] [datetime] NULL,
	[offTime] [datetime] NULL,
	[loAction] [nchar](5) NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbLogonLogs] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbLogonLogs] UNIQUE NONCLUSTERED 
(
	[loSN] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbMenus]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbMenus](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[mnuCode] [char](20) NULL,
	[mnuName] [nchar](16) NULL,
	[mnuDesc] [nvarchar](40) NULL,
	[PID] [uniqueidentifier] NULL,
	[funID] [uniqueidentifier] NULL,
	[mnuUrl] [varchar](255) NULL,
	[mnuOrder] [int] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[mnuIcon] [nchar](20) NULL,
 CONSTRAINT [PK_tbMenus] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbMenus_GE]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbMenus_GE](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[mnuCode] [char](20) NULL,
	[mnuName] [nchar](16) NULL,
	[mnuDesc] [nvarchar](40) NULL,
	[PID] [uniqueidentifier] NULL,
	[funID] [uniqueidentifier] NULL,
	[mnuUrl] [varchar](255) NULL,
	[mnuOrder] [int] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[mnuIcon] [nchar](20) NULL,
 CONSTRAINT [PK_tbMenus_GE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbModules]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbModules](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[mdCode] [char](20) NULL,
	[mdName] [nchar](16) NULL,
	[mdDesc] [nvarchar](40) NULL,
	[mdOrder] [int] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbModules] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbModules_GE]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbModules_GE](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[mdCode] [char](20) NULL,
	[mdName] [nchar](16) NULL,
	[mdDesc] [nvarchar](40) NULL,
	[mdOrder] [int] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbModules_GE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbOperateLogs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbOperateLogs](
	[isDel] [bit] NULL,
	[recSN] [timestamp] NULL,
	[ID] [uniqueidentifier] NOT NULL,
	[OperateUserName] [nvarchar](50) NULL,
	[OperateUserCode] [nvarchar](50) NULL,
	[OperateUserNumber] [nvarchar](50) NULL,
	[OperateUserDepartment] [nvarchar](50) NULL,
	[OperateModule] [nvarchar](50) NULL,
	[OperateType] [nvarchar](50) NULL,
	[OperateTime] [datetime] NULL,
	[OperateObject] [nvarchar](50) NULL,
	[OperateDetail] [nvarchar](500) NULL,
 CONSTRAINT [PK_tbOperateLog] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbRoleFuncs]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbRoleFuncs](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[rName] [nchar](10) NOT NULL,
	[funID] [uniqueidentifier] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbRoleFuncs] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbRoleFuncs] UNIQUE NONCLUSTERED 
(
	[rName] ASC,
	[funID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbRoleFuncs_GE]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbRoleFuncs_GE](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[rName] [nchar](10) NOT NULL,
	[funID] [uniqueidentifier] NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbRoleFuncs_GE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbRoleFuncs_GE] UNIQUE NONCLUSTERED 
(
	[rName] ASC,
	[funID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbRoles]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbRoles](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[rName] [nchar](10) NOT NULL,
	[rDesc] [nchar](40) NULL,
	[uCode] [char](12) NOT NULL,
	[rEditTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbRoles] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbRoles] UNIQUE NONCLUSTERED 
(
	[rName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbRoles_GE]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbRoles_GE](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[rName] [nchar](10) NOT NULL,
	[rDesc] [nchar](40) NULL,
	[uCode] [char](12) NOT NULL,
	[rEditTime] [datetime] NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
 CONSTRAINT [PK_tbRoles_GE] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbRoles_GE] UNIQUE NONCLUSTERED 
(
	[rName] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbUnits]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbUnits](
	[ID] [uniqueidentifier] NOT NULL,
	[unitCode] [nvarchar](50) NOT NULL,
	[maxUserNumber] [int] NOT NULL,
	[maxVideoUserNumber] [int] NOT NULL,
	[unitName] [nvarchar](50) NOT NULL,
	[unitLoginName] [nvarchar](50) NOT NULL,
	[uHeadPortrait] [nvarchar](50) NULL,
	[PIC] [nvarchar](50) NULL,
	[Duty] [nvarchar](50) NULL,
	[Tel] [nvarchar](50) NULL,
	[Province] [nvarchar](50) NULL,
	[City] [nvarchar](50) NULL,
	[County] [nvarchar](50) NULL,
	[detailAddress] [nvarchar](50) NULL,
	[Remarks] [nvarchar](200) NULL,
 CONSTRAINT [PK__tbUnitda__3214EC27075714DC] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbUserIMDatas]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbUserIMDatas](
	[ID] [uniqueidentifier] NOT NULL,
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[LastModifyTime] [datetime] NULL,
	[uCode] [varchar](16) NOT NULL,
	[csCode] [varchar](24) NOT NULL,
	[latitude] [decimal](10, 7) NOT NULL,
	[longitude] [decimal](10, 7) NOT NULL,
	[positionName] [varchar](260) NOT NULL,
	[altitude] [decimal](10, 5) NULL,
	[speed] [decimal](10, 5) NULL,
	[accelerationX] [decimal](10, 5) NULL,
	[accelerationY] [decimal](10, 5) NULL,
	[accelerationZ] [decimal](10, 5) NULL,
PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[tbUsers]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[tbUsers](
	[isDel] [bit] NOT NULL,
	[recSN] [timestamp] NOT NULL,
	[uCode] [char](24) NOT NULL,
	[uBelong] [char](10) NULL,
	[uIsActive] [bit] NULL,
	[rName] [nchar](10) NULL,
	[pcNum] [nvarchar](50) NULL,
	[uName] [nvarchar](30) NULL,
	[uSex] [tinyint] NOT NULL,
	[uDuty] [nchar](8) NULL,
	[dCode] [nvarchar](50) NOT NULL,
	[uTel] [char](20) NULL,
	[uShortNum] [char](8) NULL,
	[uPassword] [char](36) NOT NULL,
	[ID] [uniqueidentifier] ROWGUIDCOL  NOT NULL,
	[uHeadPortrait] [varchar](300) NULL,
	[dName] [nchar](16) NULL,
	[LYCID] [varchar](50) NULL,
	[loginFailTimes] [smallint] NOT NULL,
	[lastLoginTime] [datetime] NULL,
	[uRemarks] [nvarchar](max) NULL,
	[Createtime] [datetime] NULL,
	[uDepartment] [nvarchar](50) NULL,
	[accountType] [smallint] NOT NULL,
	[uEmployeenum] [nvarchar](50) NULL,
	[uIshistory] [bit] NULL,
	[uIsUnilt] [bit] NULL,
	[uIsAccontion] [bit] NULL,
	[uUnitCode] [varchar](50) NULL,
 CONSTRAINT [PK_tbUsers] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
 CONSTRAINT [CK_tbUsers] UNIQUE NONCLUSTERED 
(
	[uCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
ALTER TABLE [dbo].[tbApiTokens] ADD  CONSTRAINT [DF_tbApiTokens_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbApiTokens] ADD  CONSTRAINT [DF_tbApiTokens_createdTime]  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbApiTokens] ADD  CONSTRAINT [DF_tbApiTokens_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbAppLogs] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbAppLogs] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbAppLogs] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbAppVersions] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbAppVersions] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbAppVersions] ADD  DEFAULT ((0)) FOR [appVersionNo]
GO
ALTER TABLE [dbo].[tbAppVersions] ADD  DEFAULT (getdate()) FOR [appPublishTime]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_camLongitude]  DEFAULT ((0)) FOR [camLongitude]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_camLatitude]  DEFAULT ((0)) FOR [camLatitude]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_camHeight]  DEFAULT ((0)) FOR [camHeight]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_camAngle]  DEFAULT ((0)) FOR [camAngle]
GO
ALTER TABLE [dbo].[tbCameras] ADD  CONSTRAINT [DF_tbCameras_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseArticles] ADD  CONSTRAINT [DF_tbCaseArticles_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseArticles] ADD  CONSTRAINT [DF_tbCaseArticles_arCount]  DEFAULT ((0)) FOR [arCount]
GO
ALTER TABLE [dbo].[tbCaseArticles] ADD  CONSTRAINT [DF_tbCaseArticles_arWeight]  DEFAULT ((0)) FOR [arWeight]
GO
ALTER TABLE [dbo].[tbCaseArticles] ADD  CONSTRAINT [DF_tbCaseArticles_arVal]  DEFAULT ((0)) FOR [arVal]
GO
ALTER TABLE [dbo].[tbCaseArticles] ADD  CONSTRAINT [DF_tbCaseArticles_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseClues] ADD  CONSTRAINT [DF_tbCaseClues_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseClues] ADD  CONSTRAINT [DF_tbCaseClues_ccCreateTime]  DEFAULT (getdate()) FOR [ccCreateTime]
GO
ALTER TABLE [dbo].[tbCaseClues] ADD  CONSTRAINT [DF_tbCaseClues_ccCheckTime]  DEFAULT (getdate()) FOR [ccCheckTime]
GO
ALTER TABLE [dbo].[tbCaseClues] ADD  CONSTRAINT [DF_tbCaseClues_ccCheckResult]  DEFAULT (N'待审') FOR [ccCheckResult]
GO
ALTER TABLE [dbo].[tbCaseClues] ADD  CONSTRAINT [DF_tbCaseClues_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseCollection] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseCollection] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseCollection] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbCaseCompanys] ADD  CONSTRAINT [DF_tbCaseCompanys_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseCompanys] ADD  CONSTRAINT [DF_tbCaseCompanys_coClass]  DEFAULT (N'其他') FOR [coClass]
GO
ALTER TABLE [dbo].[tbCaseCompanys] ADD  CONSTRAINT [DF_tbCaseCompan_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseDeployDevs] ADD  CONSTRAINT [DF_tbCaseDeployDevs_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseDeployDevs] ADD  CONSTRAINT [DF_tbCaseDeployDevs_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseDeploys] ADD  CONSTRAINT [DF_tbCaseDeploys_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseDeploys] ADD  CONSTRAINT [DF_tbCaseDeploys_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseGoings] ADD  CONSTRAINT [DF_tbCaseGoings_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseGoings] ADD  CONSTRAINT [DF_tbCaseGoings_cgCreateTime]  DEFAULT (getdate()) FOR [cgCreateTime]
GO
ALTER TABLE [dbo].[tbCaseGoings] ADD  CONSTRAINT [DF_tbCaseGoings_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseGoings] ADD  DEFAULT ((0)) FOR [PositionLongitude]
GO
ALTER TABLE [dbo].[tbCaseGoings] ADD  DEFAULT ((0)) FOR [PositionLatitude]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_gpsTargetType]  DEFAULT (N'人') FOR [gpsTargetType]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_gpsLongitude]  DEFAULT ((0)) FOR [gpsLongitude]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_gpsLatitude]  DEFAULT ((0)) FOR [gpsLatitude]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_gpsTime]  DEFAULT (getdate()) FOR [gpsTime]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  CONSTRAINT [DF_tbCaseGps_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  DEFAULT ((0)) FOR [altitude]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  DEFAULT ((0)) FOR [speed]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  DEFAULT ((0)) FOR [accelerationX]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  DEFAULT ((0)) FOR [accelerationY]
GO
ALTER TABLE [dbo].[tbCaseGps] ADD  DEFAULT ((0)) FOR [accelerationZ]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_gunPointSize]  DEFAULT ((0)) FOR [gunPointSize]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_gunIssueDate]  DEFAULT (getdate()) FOR [gunIssueDate]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_gunIsReg]  DEFAULT ((0)) FOR [gunIsReg]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_gunBulletCount]  DEFAULT ((0)) FOR [gunBulletCount]
GO
ALTER TABLE [dbo].[tbCaseGuns] ADD  CONSTRAINT [DF_tbCaseGuns_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT ((0)) FOR [livingState]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT (getdate()) FOR [startTime]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT ('phone') FOR [sourceType]
GO
ALTER TABLE [dbo].[tbCaseLivingShows] ADD  DEFAULT ((0)) FOR [Cumulative]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  CONSTRAINT [DF_tbCaseMessages_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  CONSTRAINT [DF_tbCaseMessages_msgTime]  DEFAULT (getdate()) FOR [msgTime]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  CONSTRAINT [DF_tbCaseMessages_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  CONSTRAINT [DF__tbCaseMes__uLati__02925FBF]  DEFAULT ((0)) FOR [uLatitude]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  CONSTRAINT [DF__tbCaseMes__uLong__038683F8]  DEFAULT ((0)) FOR [uLongitude]
GO
ALTER TABLE [dbo].[tbCaseMessages] ADD  DEFAULT ('') FOR [msgFromType]
GO
ALTER TABLE [dbo].[tbCaseMission] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseMission] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseMission] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbCaseMission] ADD  DEFAULT ((0)) FOR [missionStatus]
GO
ALTER TABLE [dbo].[tbCaseMission] ADD  DEFAULT ((5)) FOR [missionLimiTime]
GO
ALTER TABLE [dbo].[tbCaseMissionDistribution] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseMissionDistribution] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseMissionDistribution] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbCaseMissionDistribution] ADD  DEFAULT ((0)) FOR [missionPersonStatus]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csTime]  DEFAULT (getdate()) FOR [csTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csTime_h]  DEFAULT (getdate()) FOR [csTime_h]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csEndTime]  DEFAULT (getdate()) FOR [csEndTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csCreateTime]  DEFAULT (getdate()) FOR [csCreateTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csRptSex]  DEFAULT ((0)) FOR [csRptSex]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csRptAge]  DEFAULT ((0)) FOR [csRptAge]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csRptTime]  DEFAULT (getdate()) FOR [csRptTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csReceptTime]  DEFAULT (getdate()) FOR [csReceptTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csDiscoverTime]  DEFAULT (getdate()) FOR [csDiscoverTime]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csRegDate]  DEFAULT (getdate()) FOR [csRegDate]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csSuspCount]  DEFAULT ((0)) FOR [csSuspCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csHurtCount]  DEFAULT ((0)) FOR [csHurtCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csDeadCount]  DEFAULT ((0)) FOR [csDeadCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csLoseVal]  DEFAULT ((0)) FOR [csLoseVal]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csCaptureVal]  DEFAULT ((0)) FOR [csCaptureVal]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csCaptCount]  DEFAULT ((0)) FOR [csCaptCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csGrpCount]  DEFAULT ((0)) FOR [csGrpCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csGrpInvCount]  DEFAULT ((0)) FOR [csGrpInvCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csRescueCount]  DEFAULT ((0)) FOR [csRescueCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csEndChainCount]  DEFAULT ((0)) FOR [csEndChainCount]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csLatitude]  DEFAULT ((0)) FOR [csLatitude]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csLongitude]  DEFAULT ((0)) FOR [csLongitude]
GO
ALTER TABLE [dbo].[tbCases] ADD  CONSTRAINT [DF_tbCases_csUpdateTime]  DEFAULT (getdate()) FOR [csUpdateTime]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_secFaceVal]  DEFAULT ((0)) FOR [secFaceVal]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_secCount]  DEFAULT ((0)) FOR [secCount]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_secVal]  DEFAULT ((0)) FOR [secVal]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_secIssueDate]  DEFAULT (getdate()) FOR [secIssueDate]
GO
ALTER TABLE [dbo].[tbCaseSecurities] ADD  CONSTRAINT [DF_tbCaseSecurities_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseSrcFiles] ADD  CONSTRAINT [DF_tbCaseSrcFiles_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseSrcFiles] ADD  CONSTRAINT [DF_tbCaseSrcFiles_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_pSex]  DEFAULT ((1)) FOR [pSex]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_spAge]  DEFAULT ((0)) FOR [spAge]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_spAge_h]  DEFAULT ((0)) FOR [spAge_h]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_spHeight]  DEFAULT ((0)) FOR [spHeight]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_spHeight_h]  DEFAULT ((0)) FOR [spHeight_h]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_arrDoDate]  DEFAULT (getdate()) FOR [arrDoDate]
GO
ALTER TABLE [dbo].[tbCaseSuspects] ADD  CONSTRAINT [DF_tbCaseSuspects_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseTrailEvs] ADD  CONSTRAINT [DF_tbCaseTrailEvs_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseTrailEvs] ADD  CONSTRAINT [DF_tbCaseTrailEvs_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseVehicles] ADD  CONSTRAINT [DF_tbCaseVehicles_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseVehicles] ADD  CONSTRAINT [DF_tbCaseVehicles_vIsFraudNum]  DEFAULT ((0)) FOR [vIsFraudNum]
GO
ALTER TABLE [dbo].[tbCaseVehicles] ADD  CONSTRAINT [DF_tbCaseVehicles_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_pSex]  DEFAULT ((1)) FOR [pSex]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_pAge]  DEFAULT ((0)) FOR [pAge]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_spAge_h]  DEFAULT ((0)) FOR [spAge_h]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_pBirth]  DEFAULT (getdate()) FOR [pBirth]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_ubDeadTime]  DEFAULT (getdate()) FOR [ubDeadTime]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_ubDeadTime_h]  DEFAULT (getdate()) FOR [ubDeadTime_h]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_ubBodyLen]  DEFAULT ((0)) FOR [ubBodyLen]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_ubFootLen]  DEFAULT ((0)) FOR [ubFootLen]
GO
ALTER TABLE [dbo].[tbCaseVictims] ADD  CONSTRAINT [DF_tbCaseVictims_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbCrimCerts] ADD  CONSTRAINT [DF_tbCrimCerts_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbCrimCerts] ADD  CONSTRAINT [DF_tbCrimCerts_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDepartments] ADD  CONSTRAINT [DF_tbDepartments_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDepartments] ADD  CONSTRAINT [DF_tbDepartments_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT (getdate()) FOR [LastModifyTime]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [latitude]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [longitude]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [altitude]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [speed]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [accelerationX]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [accelerationY]
GO
ALTER TABLE [dbo].[tbDeviceIMDatas] ADD  DEFAULT ((0)) FOR [accelerationZ]
GO
ALTER TABLE [dbo].[tbDevices] ADD  CONSTRAINT [DF_tbDevices_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDevices] ADD  CONSTRAINT [DF_tbDevices_devPDate]  DEFAULT (getdate()) FOR [devPDate]
GO
ALTER TABLE [dbo].[tbDevices] ADD  CONSTRAINT [DF_tbDevices_devGPeriod]  DEFAULT ((0)) FOR [devGPeriod]
GO
ALTER TABLE [dbo].[tbDevices] ADD  CONSTRAINT [DF_tbDevices_devSTime]  DEFAULT (getdate()) FOR [devSTime]
GO
ALTER TABLE [dbo].[tbDevices] ADD  CONSTRAINT [DF_tbDevices_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDictFields] ADD  CONSTRAINT [DF_tbDictFields_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDictFields] ADD  CONSTRAINT [DF_tbDictFields_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDictItems] ADD  CONSTRAINT [DF_tbDictItems_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDictItems] ADD  CONSTRAINT [DF_tbDictItems_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDicts] ADD  CONSTRAINT [DF_tbDicts_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDicts] ADD  CONSTRAINT [DF_tbDicts_dicLevel]  DEFAULT ((0)) FOR [dicLevel]
GO
ALTER TABLE [dbo].[tbDicts] ADD  CONSTRAINT [DF_tbDicts_dicEditTime]  DEFAULT (getdate()) FOR [dicEditTime]
GO
ALTER TABLE [dbo].[tbDicts] ADD  CONSTRAINT [DF_tbDicts_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDiscussionGroupMenbers] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDiscussionGroupMenbers] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDiscussionGroupMenbers] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbDiscussionGroups] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbDiscussionGroups] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbDiscussionGroups] ADD  DEFAULT (getdate()) FOR [createdTime]
GO
ALTER TABLE [dbo].[tbEndChainCases] ADD  CONSTRAINT [DF_tbEndChainCases_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbEndChainCases] ADD  CONSTRAINT [DF_tbEndChainCases_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbFileDetails] ADD  CONSTRAINT [DF_tbFileDetails_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbFileDetails] ADD  CONSTRAINT [DF_tbFileDetails_fSize]  DEFAULT ((0)) FOR [fSize]
GO
ALTER TABLE [dbo].[tbFileDetails] ADD  CONSTRAINT [DF_tbFileDetails_fStartTime]  DEFAULT (getdate()) FOR [fStartTime]
GO
ALTER TABLE [dbo].[tbFileDetails] ADD  CONSTRAINT [DF_tbFileDetails_fEndTime]  DEFAULT (getdate()) FOR [fEndTime]
GO
ALTER TABLE [dbo].[tbFileDetails] ADD  CONSTRAINT [DF_tbFileDetails_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbFunctions] ADD  CONSTRAINT [DF_tbFunctions_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbFunctions] ADD  CONSTRAINT [DF_tbFunctions_funOrder]  DEFAULT ((0)) FOR [funOrder]
GO
ALTER TABLE [dbo].[tbFunctions] ADD  CONSTRAINT [DF_tbFunctions_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbFunctions] ADD  DEFAULT ((1)) FOR [funType]
GO
ALTER TABLE [dbo].[tbFunctions_GE] ADD  CONSTRAINT [DF_tbFunctions_GE_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbFunctions_GE] ADD  CONSTRAINT [DF_tbFunctions_GE_funOrder]  DEFAULT ((0)) FOR [funOrder]
GO
ALTER TABLE [dbo].[tbFunctions_GE] ADD  CONSTRAINT [DF_tbFunctions_GE_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbFunctions_GE] ADD  DEFAULT ((1)) FOR [funType]
GO
ALTER TABLE [dbo].[tbLingYangDevices] ADD  DEFAULT ((0)) FOR [state]
GO
ALTER TABLE [dbo].[tbLingYangDevices] ADD  DEFAULT ((0)) FOR [conn_key]
GO
ALTER TABLE [dbo].[tbLingYangDevices] ADD  DEFAULT (getdate()) FOR [caretedTime]
GO
ALTER TABLE [dbo].[tbLingYangDevices] ADD  DEFAULT (getdate()) FOR [modifyTime]
GO
ALTER TABLE [dbo].[tbLogonLogs] ADD  CONSTRAINT [DF_tbLogonLogs_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbLogonLogs] ADD  CONSTRAINT [DF_tbLogonLogs_onTime]  DEFAULT (getdate()) FOR [onTime]
GO
ALTER TABLE [dbo].[tbLogonLogs] ADD  CONSTRAINT [DF_tbLogonLogs_offTime]  DEFAULT (getdate()) FOR [offTime]
GO
ALTER TABLE [dbo].[tbLogonLogs] ADD  CONSTRAINT [DF_tbLogonLogs_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbMenus] ADD  CONSTRAINT [DF_tbMenus_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbMenus] ADD  CONSTRAINT [DF_tbMenus_mnuOrder]  DEFAULT ((0)) FOR [mnuOrder]
GO
ALTER TABLE [dbo].[tbMenus] ADD  CONSTRAINT [DF_tbMenus_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbMenus_GE] ADD  CONSTRAINT [DF_tbMenus_GE_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbMenus_GE] ADD  CONSTRAINT [DF_tbMenus_GE_mnuOrder]  DEFAULT ((0)) FOR [mnuOrder]
GO
ALTER TABLE [dbo].[tbMenus_GE] ADD  CONSTRAINT [DF_tbMenus_GE_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbModules] ADD  CONSTRAINT [DF_tbModules_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbModules] ADD  CONSTRAINT [DF_tbModules_mdOrder]  DEFAULT ((0)) FOR [mdOrder]
GO
ALTER TABLE [dbo].[tbModules] ADD  CONSTRAINT [DF_tbModules_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbModules_GE] ADD  CONSTRAINT [DF_tbModules_GE_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbModules_GE] ADD  CONSTRAINT [DF_tbModules_GE_mdOrder]  DEFAULT ((0)) FOR [mdOrder]
GO
ALTER TABLE [dbo].[tbModules_GE] ADD  CONSTRAINT [DF_tbModules_GE_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbRoleFuncs] ADD  CONSTRAINT [DF_tbRoleFuncs_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbRoleFuncs] ADD  CONSTRAINT [DF_tbRoleFuncs_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbRoleFuncs_GE] ADD  CONSTRAINT [DF_tbRoleFuncs_GE_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbRoleFuncs_GE] ADD  CONSTRAINT [DF_tbRoleFuncs_GE_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbRoles] ADD  CONSTRAINT [DF_tbRoles_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbRoles] ADD  CONSTRAINT [DF_tbRoles_rEditTime]  DEFAULT (getdate()) FOR [rEditTime]
GO
ALTER TABLE [dbo].[tbRoles] ADD  CONSTRAINT [DF_tbRoles_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbRoles_GE] ADD  CONSTRAINT [DF_tbRoles_GE_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbRoles_GE] ADD  CONSTRAINT [DF_tbRoles_GE_rEditTime]  DEFAULT (getdate()) FOR [rEditTime]
GO
ALTER TABLE [dbo].[tbRoles_GE] ADD  CONSTRAINT [DF_tbRoles_GE_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT (getdate()) FOR [LastModifyTime]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [latitude]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [longitude]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [altitude]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [speed]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [accelerationX]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [accelerationY]
GO
ALTER TABLE [dbo].[tbUserIMDatas] ADD  DEFAULT ((0)) FOR [accelerationZ]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_isDel]  DEFAULT ((0)) FOR [isDel]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_uIsActive]  DEFAULT ((1)) FOR [uIsActive]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_uSex]  DEFAULT ((0)) FOR [uSex]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_uPassword]  DEFAULT ('0C259444075AB05321BE832D0C44A8BF') FOR [uPassword]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_ID]  DEFAULT (newsequentialid()) FOR [ID]
GO
ALTER TABLE [dbo].[tbUsers] ADD  CONSTRAINT [DF_tbUsers_loginFailTimes]  DEFAULT ((0)) FOR [loginFailTimes]
GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_AddPoliceForceRecord]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		hanyuliang
-- Create date: 2016-10-24
-- Description:	添加警力部署记录
--exec sp_ccs_AddPoliceForceRecord 's2MB60HqYpjhAg3SGZuxvw9e',"DgJontFPdz8ZuSAC,lianghanyu  ,liaoyuqi    ,wangxin     ","00012,QlFcmij4FoLv,QlFcmij4FoLd"
-- =============================================
CREATE PROCEDURE [dbo].[sp_ccs_AddPoliceForceRecord] 
    @csCode varchar(24),--案件编号
	@strPerson varchar(8000),--警员编码
	@strDev varchar(8000)--设备编码
AS

BEGIN 
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
    --警员临时表
	create table #T_Person
	(
	id int primary key not null identity (1,1),
    pCode varchar(20)
	)  
	--设备临时表
	create table #T_Dve
	(
	id int primary key not null identity (1,1),
    devCode varchar(20)
	)
	--填充临时表
	insert into #T_Person select * from dbo.f_split(@strPerson,',')
	insert into #T_Dve select * from dbo.f_split(@strDev,',') 
	--属性定义
	declare @p_count int, --定义警员表总记录数
	        @p_init int=1,--警员初始化id
	        @d_count int, --定义设备表总记录数
	        @d_init int=1 --设备初始化id
	        
	--查总记录
	select @p_count=COUNT(1) from #T_Person
	select @d_count=COUNT(1) from #T_Dve
	--添加警员记录到tbCaseDeploys表
	--定义警员属性
	declare @u_count int,@u_uCode varchar(24),@u_uDuty varchar(20)
	--定义设备属性
	declare @dev_count int,@dev_devCode varchar(24)
	while @p_init<@p_count+1
	  begin
	     select @u_uCode=uCode,@u_uDuty=uDuty
	                from dbo.tbUsers
	                       where uCode=(select pCode from #T_Person  where id=@p_init)
	     select @u_count=COUNT(1) from dbo.tbCaseDeploys where csCode=@csCode and uCode=@u_uCode
	       if @u_count<1 and @u_uCode is not null
	          begin
	              insert into dbo.tbCaseDeploys(csCode,uCode,rName)values(@csCode,@u_uCode,@u_uDuty)
	          end
	   
	     set @p_init=@p_init+1
	  end
	--添加设备记录到tbCaseDeployDevs表
	while @d_init<@d_count+1
	  begin
	      select @dev_devCode=devCode
	             from dbo.tbDevices 
	                 where devCode=(select devCode from #T_Dve  where id=@d_init)
	      select @dev_count=COUNT(1) from dbo.tbCaseDeployDevs where csCode=@csCode and devCode=@dev_devCode
	      if @dev_count<1 and @dev_devCode is not null
	         begin
	            insert into dbo.tbCaseDeployDevs(csCode,devCode)values(@csCode,@dev_devCode)
	         end
	     
	     set @d_init=@d_init+1
	  end
	drop table #T_Person
	drop table #T_Dve
END


GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_ChooseDevP]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		hanyuliang
-- Create date: 2016-10-27
-- Description:	警力部署-关联选择
-- =============================================
CREATE PROCEDURE [dbo].[sp_ccs_ChooseDevP] 
	@csCode varchar(24),--案件代号
	@devCode varchar(24),--设备编号
	@resCode varchar(24),--选择人员编号
	@retVal int OUTPUT	--若关联成功返回1，否则返回0
AS
BEGIN
	declare @count int--案件对应的设备表总记录
	declare @pCount int--案件对应的警力部署表没有添加相关警员记录
	select @pCount=COUNT(1) from dbo.tbCaseDeploys where csCode=@csCode and uCode=@resCode
	select @count=COUNT(1) from dbo.tbCaseDeployDevs where csCode=@csCode and devCode=@devCode
	if @pCount>0
	   begin 
	       	if @count>0--若没有添加设备记录，则不操作
			   begin
				 update dbo.tbCaseDeployDevs set uCode=@resCode where  csCode=@csCode and devCode=@devCode
				 set @retVal=1
			   end
			else
				 set @retVal=0
	   end
	else
	    set @retVal=2

END
--select * from dbo.tbCaseDeploys where csCode='A4400000000002016100018'

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_DeleteCaseInfo]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		hanyuliang
-- Create date: <2016-10-18>
-- Description:	<删除案件信息>
-- =============================================
CREATE PROCEDURE [dbo].[sp_ccs_DeleteCaseInfo]
	@csCode varchar(24)
AS
BEGIN
    delete from dbo.tbCaseSuspects where csCode=@csCode
	delete from dbo.tbCaseDeploys where csCode=@csCode
	delete from dbo.tbCaseDeployDevs where csCode=@csCode
	delete from dbo.tbCaseGoings where csCode=@csCode
	delete from dbo.tbCases where csCode=@csCode

END


GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_NewCode]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Xenia,,>
-- Create date: <2016-09-26,,>
-- Description:	<Generate new code,,>
-- =============================================
CREATE PROCEDURE [dbo].[sp_ccs_NewCode]
	-- Add the parameters for the stored procedure here
	@sType char(3),		/* 输入参数，编号类型，3位长代码（英文单词前3字母），可能是下方列表中的任一个值
						--------------------------------------------------------
						类型	名称			前缀	表/字段
						--------------------------------------------------------
						CAS		案件编号		A		tbCases/csCode
						SUS		嫌疑人编号		R		tbCaseSuspects/pCode
						FIN		指纹编号		Z		tbCaseSuspects/spFprCode
						VEH		嫌疑车编号		VE		tbCaseVehicles/vCode
						TRA		痕迹物证编号	EV		tbCaseTrailEvs/evCode
						GUN		枪弹编号		GU		tbCaseGuns/gunCode
						SEC		证劵编号		SE		tbCaseSecurities/secCode
						ART		物品编号		AR		tbCaseArticles/arCode
						VIC		受害人编号		VT		tbCaseVictims/vtCode
						COM		犯罪单位编号	CO		tbCompany/coCode 
						VCO		受害单位编号	VC		tbCompany/coCode
						OCO		其它单位编号	OC		tbCompany/coCode

						FSN		文件编号				tbFileDetails/fCode
						RSN		记录集编号				tbFileDetails/attRsCode
						AAA		员工工号			    tbUsers/pcNum 当accountType=0（后台添加）时进行编号
						--------------------------------------------------------*/
	@sInstCode char(12),		/* 输入参数，12位机构代码,如广东省公安厅为440000000000 */
	@retVal char(24) OUTPUT		/* 输出参数，返回的新编号，国标为一字母前缀，其他自定义编号为两字母前缀，输入参数无效时返回空字符串 */
AS

BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;
    -- Insert statements for procedure here
	--SELECT <@Param1, sysname, @p1>, <@Param2, sysname, @p2>
	DECLARE --@sCRLF char(2),    
	--@sTAB char(1),
	@sPrefix char(2),
	@sDate char(8),
	@sSN char(6)

	--SET @sTAB = char(9)
	--SET @sCRLF = char(13) + char (10)
	SET @sDate = CONVERT(varchar(6), GETDATE(), 112)
	SET @sSN='0000'
	SET @retVal=''

	IF @sType = ''
		RETURN

	-----------------------------------------------------------------------------------
	IF @sType = 'FSN' OR @sType = 'RSN'
	BEGIN
		SET @sDate = CONVERT(varchar(8), GETDATE(), 112)
		IF @sType = 'FSN'
		BEGIN
			IF(
				SELECT MAX(fCode) FROM tbFileDetails WHERE SUBSTRING(fCode,1,8)=CONVERT(varchar(8), GETDATE(), 112)
			) IS NULL
				SET @sSN='000001'
			ELSE
				SELECT @sSN=RIGHT('000000'+CONVERT(varchar(6),CAST(MAX(SUBSTRING(fCode,9,6)) AS smallint)+1),6) FROM tbFileDetails WHERE SUBSTRING(fCode,1,8)=CONVERT(varchar(8), GETDATE(), 112)
		END
		ELSE IF @sType = 'RSN'
		BEGIN
			IF(
				SELECT MAX(attRsCode) FROM tbFileDetails WHERE SUBSTRING(attRsCode,1,8)=CONVERT(varchar(8), GETDATE(), 112)
			) IS NULL
				SET @sSN='0001'
			ELSE
				SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(attRsCode,9,4)) AS smallint)+1),4) FROM tbFileDetails WHERE SUBSTRING(attRsCode,1,8)=CONVERT(varchar(8), GETDATE(), 112)
		END

		SET @retVal = @sDate + @sSN
		RETURN
	END
	-----------------------------------------------------------------------------------

	IF @sInstCode = '' OR LEN(@sInstCode) <> 12
		RETURN

	/*IF(
		SELECT dicCode FROM tbDicts WHERE RTRIM(dicFather)<>'0' AND LEN(RTRIM(dicCode))=12 AND RTRIM(dicField)='PSI_Name' AND RTRIM(dicCode)=RTRIM(@sInstCode)
		) IS NULL	--Institution not found
		RETURN*/

	IF @sType = 'CAS'    --tbCases csCode
	BEGIN
		SET @sPrefix = 'A'
		IF(
			SELECT MAX(csCode) FROM tbCases WHERE SUBSTRING(csCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(csCode,20,4)) AS smallint)+1),4) FROM tbCases WHERE SUBSTRING(csCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
	--RETURN    
	END
	ELSE IF @sType = 'SUS'	--tbCaseSuspects pCode
	BEGIN
		SET @sPrefix = 'R'
		IF(
			SELECT MAX(pCode) FROM tbCaseSuspects WHERE SUBSTRING(pCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(pCode,20,4)) AS smallint)+1),4) FROM tbCaseSuspects WHERE SUBSTRING(pCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'FIN'	--tbCaseSuspects spFprCode
	BEGIN
		SET @sPrefix = 'Z'
		IF(
			SELECT MAX(spFprCode) FROM tbCaseSuspects WHERE SUBSTRING(spFprCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(spFprCode,20,4)) AS smallint)+1),4) FROM tbCaseSuspects WHERE SUBSTRING(spFprCode,14,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'VEH'	--tbCaseVehicles vCode
	BEGIN
		SET @sPrefix = 'VE'
		IF(
			SELECT MAX(vCode) FROM tbCaseVehicles WHERE SUBSTRING(vCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(vCode,21,4)) AS smallint)+1),4) FROM tbCaseVehicles WHERE SUBSTRING(vCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'TRA'	--tbCaseTrailEvs evCode
	BEGIN
		SET @sPrefix = 'EV'
		IF(
			SELECT MAX(evCode) FROM tbCaseTrailEvs WHERE SUBSTRING(evCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(evCode,21,4)) AS smallint)+1),4) FROM tbCaseTrailEvs WHERE SUBSTRING(evCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'GUN'	--tbCaseGuns gunCode
	BEGIN
		SET @sPrefix = 'GU'
		IF(
			SELECT MAX(gunCode) FROM tbCaseGuns WHERE SUBSTRING(gunCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(gunCode,21,4)) AS smallint)+1),4) FROM tbCaseGuns WHERE SUBSTRING(gunCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'SEC'	--tbCaseSecurities secCode
	BEGIN
		SET @sPrefix = 'SE'
		IF(
			SELECT MAX(secCode) FROM tbCaseSecurities WHERE SUBSTRING(secCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(secCode,21,4)) AS smallint)+1),4) FROM tbCaseSecurities WHERE SUBSTRING(secCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'ART'	--tbCaseArticles arCode
	BEGIN
		SET @sPrefix = 'AR'
		IF(
			SELECT MAX(arCode) FROM tbCaseArticles WHERE SUBSTRING(arCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(arCode,21,4)) AS smallint)+1),4) FROM tbCaseArticles WHERE SUBSTRING(arCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'VIC'	--tbCaseVictims vtCode
	BEGIN
		SET @sPrefix = 'VT'
		IF(
			SELECT MAX(vtCode) FROM tbCaseVictims WHERE SUBSTRING(vtCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(vtCode,21,4)) AS smallint)+1),4) FROM tbCaseVictims WHERE SUBSTRING(vtCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'COM'	--tbCaseCompanys coCode
	BEGIN
		SET @sPrefix = 'CO'
		IF(
			SELECT MAX(coCode) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='犯罪单位'
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(coCode,21,4)) AS smallint)+1),4) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='犯罪单位'
	END
	ELSE IF @sType = 'VCO'	--tbCaseCompanys coCode
	BEGIN
		SET @sPrefix = 'VC'
		IF(
			SELECT MAX(coCode) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='受害单位'
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(coCode,21,4)) AS smallint)+1),4) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='受害单位'
	END
	ELSE IF @sType = 'OCO'	--tbCaseCompanys coCode
	BEGIN
		SET @sPrefix = 'OC'
		IF(
			SELECT MAX(coCode) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='其他'
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(coCode,21,4)) AS smallint)+1),4) FROM tbCaseCompanys WHERE SUBSTRING(coCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) AND coClass='其他'
	END
	ELSE IF @sType = 'ODO'	
	BEGIN
		SET @sPrefix = 'OD'
		IF(
			SELECT MAX(dCode) FROM tbDepartments WHERE SUBSTRING(dCode,15,6)=CONVERT(varchar(6), GETDATE(), 112) 
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(dCode,21,4)) AS smallint)+1),4) FROM tbDepartments WHERE SUBSTRING(dCode,15,6)=CONVERT(varchar(6), GETDATE(), 112)
	END
	ELSE IF @sType = 'AAA'    --tbCases csCode
	BEGIN
		SET @sPrefix = 'A'
		IF(
			SELECT MAX(pcNum) FROM tbUsers WHERE SUBSTRING(pcNum,14,6)=CONVERT(varchar(6), GETDATE(), 112)
		) IS NULL
			SET @sSN='0001'
		ELSE
			SELECT @sSN=RIGHT('0000'+CONVERT(varchar(4),CAST(MAX(SUBSTRING(pcNum,20,4)) AS smallint)+1),4) FROM tbUsers WHERE SUBSTRING(pcNum,14,6)=CONVERT(varchar(6), GETDATE(), 112)
	
	END
	ELSE
		RETURN

	SET @retVal = RTRIM(@sPrefix) + RTRIM(@sInstCode) + RTRIM(@sDate) + RTRIM(@sSN)
END 




GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_Organization]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_Organization]	
      @AccountString varchar(max)  --需要截取的字符串,外部传入的查询条件     
AS
      DECLARE @TempSql varchar(max)      --临时存放sql查询语句 
      DECLARE @SqlCondition varchar(max) --临时存放sql条件语句
      DECLARE @SqlConditionsecond varchar(max) --临时存放sql条件语句,
     
BEGIN
     declare  @Account varchar(max)    --截取后的字符 
     declare  @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
     declare  @SplitCharPos int        --记录截取位置
     set   @SplitChar=','
     set @SplitCharPos = 0;           --初始截取位置为0 
     set @SqlCondition = '';          
     
     select @SplitCharPos = CHARINDEX(@SplitChar, @AccountString)  --第一次出现的位置  
    if @SplitCharPos = 0                                               --如果第一次出现位置为0,说明只有一个条件包含两个参数  
    begin   
     select @TempSql='select * from tbDepartments m left join tbDepartments n on m.dFather=n.dCode where m.ID='+''''+@AccountString+''''
    end   
    else   
        begin   
           while @SplitCharPos <> 0           --直到出现的位置不为0  
            begin   
                select @SplitCharPos = CHARINDEX(@SplitChar, @AccountString)     
                if @SplitCharPos = 0   
                begin   
                    set @Account = @AccountString   
                end   
                else   
                    begin 
                    select @Account = LEFT(@AccountString, @SplitCharPos - 1)   --循环取到的字符串信息
                    select @AccountString = RIGHT(@AccountString, len(@AccountString)-len(@Account) - 1)                        
                    select @SqlCondition= @SqlCondition +'m.ID='+''''+ @Account +''''+' or '                     
            end                                          
           end 
                
      select @SqlCondition= @SqlCondition +'m.ID='+''''+ @AccountString +''''               
            
    select @TempSql='select * from tbDepartments m left join tbDepartments n on m.dFather=n.dCode where ('+@SqlCondition+')'
   end
   exec(@TempSql)
end

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_PoliceDispatch]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_PoliceDispatch]	
       @AccountString varchar(2000)  --需要截取的字符串,外部传入的查询条件 
        
AS
      DECLARE @TempSql varchar(2000)      --临时存放sql查询语句 
      DECLARE @SqlCondition varchar(2000) --临时存放sql条件语句
      DECLARE @SqlConditionsecond varchar(3000) --临时存放sql条件语句,
BEGIN
	declare  @Account varchar(200)    --截取后的字符 
	declare  @AccountF varchar(200)    --截取后的字符
	declare  @AccountS varchar(200)    --截取后的字符 
    declare  @SplitCharPos int        --记录截取位置
    declare  @SplitCharPosinner int        --记录截取位置   
    declare  @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
    declare @SplitCharfirst varchar(10)     --要截取的分隔符，外部进行定义
    set   @SplitChar=','
    set   @SplitCharfirst=';'
    set @SplitCharPos = 0;           --初始截取位置为0 
    set @SplitCharPosinner = 0;           --初始截取位置为0 
    set @SqlCondition = '';             
    select @SplitCharPos = CHARINDEX(@SplitCharfirst, @AccountString)  --第一次出现的位置  
    if @SplitCharPos = 0                                               --如果第一次出现位置为0,说明只有一个条件包含两个参数  
    begin   
            select @SplitCharPosinner = CHARINDEX(@SplitChar, @AccountString)
            select @AccountF = LEFT(@AccountString, @SplitCharPosinner - 1)   --循环取到的字符串信息
            select @AccountS = RIGHT(@AccountString, len(@AccountString)-len(@AccountF) - 1)
            select @SqlCondition= @SqlCondition +'csCode='+''''+ @AccountF +''''+' and uCode='+''''+ @AccountS +''''
            select @TempSql='select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode where gpsTime in (select max(gpsTime) from tbCaseGps t where '+@SqlCondition+ ' group by uCode)'
            --select @TempSql='select * from (select s.csCode,s.uCode,s.gpsLongitude,s.gpsLatitude,s.gpsTime,m.devCode,s.ID,s.isDel,s.recSN,s.gpsTargetType from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode where gpsTime in (select max(gpsTime) from tbCaseGps t where '+@SqlCondition+' group by uCode))a'
            --select @Account = @AccountString    --原字符串等于截取后的字符  
            --select @Account as Account          --查出要截取的字符  
    end   
    else   
        begin   
           while @SplitCharPos <> 0           --直到出现的位置不为0  
            begin   
                select @SplitCharPos = CHARINDEX(@SplitCharfirst, @AccountString)     
                if @SplitCharPos = 0   
                begin   
                    set @Account = @AccountString   
                end   
                else   
                    begin 
                    select @Account = LEFT(@AccountString, @SplitCharPos - 1)   --循环取到的字符串信息
                    select @AccountString = RIGHT(@AccountString, len(@AccountString)-len(@Account) - 1)
                    
                    select @SplitCharPosinner = CHARINDEX(@SplitChar, @Account)
                    select @AccountF = LEFT(@Account, @SplitCharPosinner - 1)   --循环取到的字符串信息
                    select @AccountS = RIGHT(@Account, len(@Account)-len(@AccountF) - 1)
                    select @SqlCondition= @SqlCondition +'(csCode='+''''+ @AccountF +''''+' and uCode='+''''+ @AccountS +''''+') or '                     
                   -- select @SqlCondition= @SqlCondition +'uCode='+''''+ @Account+''''+' or '   
            end                                          
           end 
          -- select @SqlCondition= @SqlCondition +'uCode='+''''+ @Account +''''
          select @SplitCharPosinner = CHARINDEX(@SplitChar, @Account)
           select @AccountF = LEFT(@AccountString, @SplitCharPosinner - 1)   --循环取到的字符串信息
           select @AccountS = RIGHT(@AccountString, len(@AccountString)-len(@AccountF) - 1)
           select @SqlCondition= @SqlCondition +'(csCode='+''''+ @AccountF +''''+' and uCode='+''''+ @AccountS +''''+')'   
           select @TempSql='select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode where gpsTime in (select max(gpsTime) from tbCaseGps t where '+@SqlCondition+ ' group by uCode)' 
           --select @TempSql='select * from (select s.csCode,s.uCode,s.gpsLongitude,s.gpsLatitude,s.gpsTime,m.devCode,s.ID,s.isDel,s.recSN,s.gpsTargetType from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode where gpsTime in (select max(gpsTime) from tbCaseGps t where '+@SqlCondition+ ' group by uCode))a'            
    end   
   execute (@TempSql)
  
END


GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_TrackRecord]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_TrackRecord]	
      @csNameTime varchar(1000)  --需要截取的字符串,外部传入的查询条件     
AS
      DECLARE @csName varchar(1000)    --临时存放字符串查询语句 
      DECLARE @stattime varchar(1000)  --临时存放字符串查询语句 
      DECLARE @endtime varchar(1000)    --临时存放字符串查询语句
      DECLARE @TempSql varchar(1000)  --临时存放sql条件语句
      declare @st datetime
      declare @et datetime
BEGIN
     declare @SplitCharPos int        --记录截取位置  
     declare @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
     set   @SplitChar=';'
     set @SplitCharPos = 0;           --初始截取位置为0 
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @csName = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @csNameTime = RIGHT(@csNameTime, len(@csNameTime)-len(@csName) - 1)
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @stattime = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @endtime = RIGHT(@csNameTime, len(@csNameTime)-len(@stattime) - 1) 
    set @st=cast(@stattime as datetime)
    set @et=cast(@endtime as datetime)
    select * from tbCaseGps where gpsTime in( select max(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
    --select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode left join tbDevices n on m.devCode=n.devCode where gpsTime in( select max(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
end

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_TrackRecord_dev]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_TrackRecord_dev]	
      @csNameTime varchar(1000)  --需要截取的字符串,外部传入的查询条件     
AS
      DECLARE @csName varchar(1000)    --临时存放字符串查询语句 
      DECLARE @stattime varchar(1000)  --临时存放字符串查询语句 
      DECLARE @endtime varchar(1000)    --临时存放字符串查询语句
      DECLARE @TempSql varchar(1000)  --临时存放sql条件语句
      declare @st datetime
      declare @et datetime
BEGIN
     declare @SplitCharPos int        --记录截取位置  
     declare @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
     set   @SplitChar=';'
     set @SplitCharPos = 0;           --初始截取位置为0 
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @csName = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @csNameTime = RIGHT(@csNameTime, len(@csNameTime)-len(@csName) - 1)
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @stattime = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @endtime = RIGHT(@csNameTime, len(@csNameTime)-len(@stattime) - 1) 
    set @st=cast(@stattime as datetime)
    set @et=cast(@endtime as datetime)
    select * from tbCaseGps where gpsTime in( select max(gpsTime) from tbCaseGps where gpsTime>@st and gpsTime<@et and devCode!='警员' group by devCode)
    --select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode left join tbDevices n on m.devCode=n.devCode where gpsTime in( select max(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
end

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_TrackRecord_dev_S]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_TrackRecord_dev_S]	
      @csNameTime varchar(1000)  --需要截取的字符串,外部传入的查询条件     
AS
      DECLARE @csName varchar(1000)    --临时存放字符串查询语句 
      DECLARE @stattime varchar(1000)  --临时存放字符串查询语句 
      DECLARE @endtime varchar(1000)    --临时存放字符串查询语句
      DECLARE @TempSql varchar(1000)  --临时存放sql条件语句
      declare @st datetime
      declare @et datetime
BEGIN
     declare @SplitCharPos int        --记录截取位置  
     declare @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
     set   @SplitChar=';'
     set @SplitCharPos = 0;           --初始截取位置为0 
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @csName = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @csNameTime = RIGHT(@csNameTime, len(@csNameTime)-len(@csName) - 1)
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @stattime = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @endtime = RIGHT(@csNameTime, len(@csNameTime)-len(@stattime) - 1) 
    set @st=cast(@stattime as datetime)
    set @et=cast(@endtime as datetime)
    select * from tbCaseGps where gpsTime in( select min(gpsTime) from tbCaseGps where  gpsTime>@st and gpsTime<@et and devCode!='警员' group by devCode)
    --select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode left join tbDevices n on m.devCode=n.devCode where gpsTime in( select max(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
end

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_TrackRecord_s]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[sp_ccs_TrackRecord_s]	
      @csNameTime varchar(1000)  --需要截取的字符串,外部传入的查询条件     
AS
      DECLARE @csName varchar(1000)    --临时存放字符串查询语句 
      DECLARE @stattime varchar(1000)  --临时存放字符串查询语句 
      DECLARE @endtime varchar(1000)    --临时存放字符串查询语句
      DECLARE @TempSql varchar(1000)  --临时存放sql条件语句
      declare @st datetime
      declare @et datetime
BEGIN
     declare @SplitCharPos int        --记录截取位置  
     declare @SplitChar varchar(10)         --要截取的分隔符，外部进行定义
     set   @SplitChar=';'
     set @SplitCharPos = 0;           --初始截取位置为0 
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @csName = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @csNameTime = RIGHT(@csNameTime, len(@csNameTime)-len(@csName) - 1)
    select @SplitCharPos = CHARINDEX(@SplitChar, @csNameTime)  --第一次出现的位置 
    select @stattime = LEFT(@csNameTime, @SplitCharPos - 1)   --循环取到的字符串信息
    select @endtime = RIGHT(@csNameTime, len(@csNameTime)-len(@stattime) - 1) 
    set @st=cast(@stattime as datetime)
    set @et=cast(@endtime as datetime)
    select * from tbCaseGps where gpsTime in( select min(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
    --select * from tbCaseGps s left join tbCaseDeployDevs m on s.csCode=m.csCode and s.uCode=m.uCode left join tbDevices n on m.devCode=n.devCode where gpsTime in( select max(gpsTime) from tbCaseGps where csCode in (select csCode from tbCases where csName=@csName) and gpsTime>@st and gpsTime<@et group by uCode)
end

GO
/****** Object:  StoredProcedure [dbo].[sp_ccs_UnChooseDevP]    Script Date: 2017/9/6 15:51:08 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		hanyuliang
-- Create date: 2016-10-27
-- Description:	警力部署-取消关联选择
-- =============================================
Create PROCEDURE [dbo].[sp_ccs_UnChooseDevP] 
	@csCode varchar(24),--案件代号
	@devCode varchar(24),--设备编号
	@retVal int OUTPUT	--若取消关联成功返回1，否则返回0
AS
BEGIN
	declare @count int
	select @count=COUNT(1) from dbo.tbCaseDeployDevs where csCode=@csCode and devCode=@devCode
	if @count>0
	   begin
	     update dbo.tbCaseDeployDevs set uCode=null where  csCode=@csCode and devCode=@devCode
	     set @retVal=1
	   end
	else
	     set @retVal=0
END


GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'直播间ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbBroadcastRoom', @level2type=N'COLUMN',@level2name=N'rCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'加入人ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbBroadcastRoom', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbBroadcastRoom', @level2type=N'COLUMN',@level2name=N'createdTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'监控点编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'监控点名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'监控类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camAddr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'经度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camLongitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'纬度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camLatitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'安装高度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camHeight'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'朝向' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'camAngle'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCameras', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'物品编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'品名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'产牌' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arBrand'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'产地' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arProdArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'型号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arModel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'规格' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arSpec'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'式样' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arPat'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'质地' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arMaterial'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'颜色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arColor'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'成色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arQuality'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arCountUnit'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'重量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arWeight'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'重量单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arWeightUnit'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'价值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arVal'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特征描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'arRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseArticles', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'线索类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'线索摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccAbstract'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'线索文件' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccFile'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'研判' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'提交时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccCreateTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'uCodeChk'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccCheckTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'审核结果' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccCheckResult'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'线索来源' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccSourceID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'来源途径' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ccSourceType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseClues', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否删除' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'isDel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'recSN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'法定代表人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coCorporateRep'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coTel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'电子邮箱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coEmail'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位行政区划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'单位详址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coAddress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'coPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseCompanys', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeployDevs', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeployDevs', @level2type=N'COLUMN',@level2name=N'devCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'负责人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeployDevs', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeployDevs', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeploys', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'人员编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeploys', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeploys', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseDeploys', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'cgType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'内容/摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'cgAbstract'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'录入人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'录入时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'cgCreateTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGoings', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'目标类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'gpsTargetType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'devCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户帐号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'经度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'gpsLongitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'纬度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'gpsLatitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'gpsTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGps', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'枪弹编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'枪型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunModel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'枪号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'口径' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunPointSize'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特征描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发证日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunIssueDate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'持枪证号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunLicenseNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'建档' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunIsReg'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'持枪人单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunOwnerWorkUnit'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'持枪人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunOwner'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'子弹数目' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunBulletCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'gunRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseGuns', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'消息类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'msgType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'消息摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'msgAbstract'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送文件' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'msgFile'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发送时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'msgTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'纬度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'uLatitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'经度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'uLongitude'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'位置名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseMessages', @level2type=N'COLUMN',@level2name=N'uPositionName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发案地点' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csAddress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发案时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发案时间上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csTime_h'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csStatus'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'简要案情' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破销结案标识' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndFlag'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破销结案时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受理单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csAccept'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'办案人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csContact'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'办案人电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csTel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'预案' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csPlan'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'创建时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCreateTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人性别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptSex'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人年龄' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptAge'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人工作单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptWorkUnit'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人居住地' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptLiveAddr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案人电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptTel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'报案时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受理人姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRecept'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受理方式' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRptType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受理时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csReceptTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发现形式案件来源' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csHowDiscover'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发现时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csDiscoverTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发案地行政区' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发案地域' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csSceneType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'危害程度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csHurtLevel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'立案日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRegDate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'立案单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRegInst'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'专案标识' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csFlag'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'办案人电子邮箱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEmail'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'补立原因' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csReregReason'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'作案人数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csSuspCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受伤人数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csHurtCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'死亡人数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csDeadCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'损失价值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csLoseVal'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'选择时机' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csChoseOpp'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'选择处所' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csChoseLoc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'选择对象' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csChoseObj'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'选择物品' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csChoseArt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'作案手段' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCrimeTrick'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'作案特点' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCrimePat'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'作案工具' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCrimeTool'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破案单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndDept'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破案方式' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndWay'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破案类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'缴获总价值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCaptureVal'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'抓获人员数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csCaptCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'集团人数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csGrpCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'集团性质' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csGrpKind'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'集团涉案数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csGrpInvCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'破案简况' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'解救人员类' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRescueType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'解救人数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csRescueCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'带破案件数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'csEndChainCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCases', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'证劵编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'有价证劵' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'面值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secFaceVal'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数量' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secCount'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'价值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secVal'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发行单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secIssue'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发行日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secIssueDate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特征描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'secRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSecurities', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'fCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'sfAbstract'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'sfType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'来源' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'sfSrcID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSrcFiles', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'人员编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'别名或绰号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pByname'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pSex'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'年龄' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spAge'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'年龄上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spAge_h'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'国籍或地区' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pCountry'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'民族' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pNation'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'身高' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spHeight'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'身高上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spHeight_h'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'公民身份号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pIdNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'职业' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pCareer'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'口音' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spAccent'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'脸型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spFaceShape'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pBodyShape'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'其他特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spOthFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特殊特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pSpclFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体表标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pSurfaceMark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'专长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spGoodAt'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'身份' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pIdentify'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'血型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pBloodType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'违法犯罪经历' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spCrimes'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'居住地行政区划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pLiveArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'居住地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pAddress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'户籍地行政区划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pBirthArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'户籍地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pBirthAddr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'指纹编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spFprCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体貌特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'spFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'穿戴特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pDress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'相片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'pPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'作案原因' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'arrMcCause'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'抓获单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'arrDoDept'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'抓获日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'arrDoDate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseSuspects', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'物证编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'evCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'痕迹名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'trName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'痕迹描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'trDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'物证名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'evName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'物证描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'evDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'细小物质' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'evMicro'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'现场照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'scnPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'现场特征描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'scnDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'evRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseTrailEvs', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'嫌疑车编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'涉案物品性质' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'车辆类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'车牌号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vCarNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'品牌' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vBrand'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'型号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vModel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'颜色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vColor'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否套牌' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vIsFraudNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特征描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'车主' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vOwner'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'车主地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vOwnerAddr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vOwnerTel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'电子邮箱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vOwnerEmail'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'号牌种类' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vCarNumType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'发动机号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vEngineNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'车架号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vVin'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'保险情况' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'vInsStatus'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVehicles', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'人员编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'vtCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'类别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'vtClass'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pSex'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'国籍或地区' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pCountry'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'民族' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pNation'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pBodyShape'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'特殊特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pSpclFeature'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'体表标记' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pSurfaceMark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'身份' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pIdentify'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'血型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pBloodType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'年龄' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pAge'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'年龄上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'spAge_h'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'职业' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pCareer'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'居住地行政区划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pLiveArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'居住地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pAddress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'户籍地行政区划' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pBirthArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'户籍地址' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pBirthAddr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'穿戴特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pDress'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'照片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'出生日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pBirth'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受害形式' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'vtAssaultMode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'人身伤害程度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'vtInjury'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'工作单位' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pWorkUnit'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'受害人电子邮箱' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pEmail'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'死亡时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubDeadTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'死亡时间上限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubDeadTime_h'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'尸长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubBodyLen'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'足长' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubFootLen'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'牙齿特征' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubTooth'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'尸体完整程度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubBodyIntegrity'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'尸体腐败程度' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubBodyFresh'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'携带物品' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ubTakeWith'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'pRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCaseVictims', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'人员编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCrimCerts', @level2type=N'COLUMN',@level2name=N'pCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'证件类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCrimCerts', @level2type=N'COLUMN',@level2name=N'pCertType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'证件号码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCrimCerts', @level2type=N'COLUMN',@level2name=N'pCertNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbCrimCerts', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'机构编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDepartments', @level2type=N'COLUMN',@level2name=N'dCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'机构名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDepartments', @level2type=N'COLUMN',@level2name=N'dName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'上级机构编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDepartments', @level2type=N'COLUMN',@level2name=N'dFather'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'机构职能' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDepartments', @level2type=N'COLUMN',@level2name=N'dDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDepartments', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'品牌' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devBrand'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'型号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devModel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'生产日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devPDate'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'使用年限' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devGPeriod'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'入库日期' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devSTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'保管人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'状态' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devStatus'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备图片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devPhoto'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'备注' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devRemark'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'设备SN' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDevices', @level2type=N'COLUMN',@level2name=N'devSN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字段名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictFields', @level2type=N'COLUMN',@level2name=N'tField'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据项名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictFields', @level2type=N'COLUMN',@level2name=N'dicField'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictFields', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictItems', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'表名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictItems', @level2type=N'COLUMN',@level2name=N'tName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'字段名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictItems', @level2type=N'COLUMN',@level2name=N'tField'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictItems', @level2type=N'COLUMN',@level2name=N'dicCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDictItems', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'数据项名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicField'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicValue'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'值描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicValDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父节点代码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicFather'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'层级' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicLevel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'拼音首字母' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicValAbbr'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'dicEditTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbDicts', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbEndChainCases', @level2type=N'COLUMN',@level2name=N'csCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'带破案件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbEndChainCases', @level2type=N'COLUMN',@level2name=N'csEndChainCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'带破案件区域' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbEndChainCases', @level2type=N'COLUMN',@level2name=N'csEndChainArea'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbEndChainCases', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'记录集编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'attRsCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件大小' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fSize'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'文件摘要' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fAbstract'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'首帧图片' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fFirstFrame'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'开始时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fStartTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'结束时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'fEndTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFileDetails', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Controller' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funController'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'动作' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funAction'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'所属模块' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'mdID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能类型（1表示模块功能；2表示操作功能）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'funType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父功能id（操作功能是模块功能的子项）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions', @level2type=N'COLUMN',@level2name=N'PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Controller' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funController'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'动作' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funAction'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'所属模块' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'mdID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能类型（1表示模块功能；2表示操作功能）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'funType'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'父功能id（操作功能是模块功能的子项）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbFunctions_GE', @level2type=N'COLUMN',@level2name=N'PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'loSN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'登入时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'onTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'登出时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'offTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'操作类型' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'loAction'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbLogonLogs', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'mnuCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'mnuName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'mnuDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Parent ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Function ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'funID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'mnuUrl'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'mnuOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'mnuCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'mnuName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'菜单描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'mnuDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Parent ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'PID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'Function ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'funID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'URL' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'mnuUrl'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'mnuOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbMenus_GE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否删除' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'isDel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'recSN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'mdCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'mdName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'mdDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'mdOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否删除' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'isDel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'流水号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'recSN'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块编码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'mdCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'mdName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'模块描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'mdDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'顺序号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'mdOrder'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbModules_GE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs', @level2type=N'COLUMN',@level2name=N'funID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs_GE', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'功能项ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs_GE', @level2type=N'COLUMN',@level2name=N'funID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoleFuncs_GE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles', @level2type=N'COLUMN',@level2name=N'rDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles', @level2type=N'COLUMN',@level2name=N'rEditTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色名称' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles_GE', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色描述' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles_GE', @level2type=N'COLUMN',@level2name=N'rDesc'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改人' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles_GE', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最后修改时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles_GE', @level2type=N'COLUMN',@level2name=N'rEditTime'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbRoles_GE', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'isDel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'用户帐号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'所属系统' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uBelong'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'是否激活' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uIsActive'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'角色' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'rName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'警员编号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'pcNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'姓名' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'性别' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uSex'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'职务' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uDuty'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'所属部门' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'dCode'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'联系电话' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uTel'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'短号' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uShortNum'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'密码' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'uPassword'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'ID' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'ID'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'部门名称（冗余字段，方便查询使用）' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'dName'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'登录失败次数' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'loginFailTimes'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'最近登录时间' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'tbUsers', @level2type=N'COLUMN',@level2name=N'lastLoginTime'
GO
