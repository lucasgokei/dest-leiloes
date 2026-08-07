-- CreateEnum
CREATE TYPE "AuctionCategory" AS ENUM ('VEICULOS', 'IMOVEIS', 'ELETRONICOS', 'OUTROS');

-- AlterTable
ALTER TABLE "Auction" ADD COLUMN "category" "AuctionCategory" NOT NULL DEFAULT 'OUTROS';

-- CreateIndex
CREATE INDEX "Auction_category_idx" ON "Auction"("category");
